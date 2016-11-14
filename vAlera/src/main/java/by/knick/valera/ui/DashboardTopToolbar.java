package by.knick.valera.ui;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import by.knick.valera.flow.Flow;
import by.knick.valera.flow.Step;
import by.knick.valera.flow.exec.ExecutionContext;
import by.knick.valera.flow.exec.StepExecutor;
import by.knick.valera.flow.exec.StepExecutorsRegistry;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import lombok.extern.slf4j.Slf4j;
import rx.subjects.PublishSubject;

@Slf4j
public class DashboardTopToolbar implements Initializable {

	@FXML
	private Button runButton;
	@FXML
	private Button stopButton;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	private ExecutorService executor = Executors.newCachedThreadPool();

	private Task<Void> executionTask = null;

	@FXML
	private void onRunBtnClicked() {
		final Flow flow = DashboardController.getInstance().getProject().getConfig().getFlow();

		final Map<String, Object> flowPayload = new LinkedHashMap<>();
		final ExecutionContext executionContext = new ExecutionContext();

		final PublishSubject<Map<String, Object>> publishSubject = PublishSubject.create();
		executionContext.setPayloadPublishSubject(publishSubject);

		DashboardController.getInstance().getPayloadPreviewWidgetController().setPayloadObservable(publishSubject);

		executionTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				StepExecutor<Step> flowExecutor = StepExecutorsRegistry.getInstance().getExecutor(flow);
				flowExecutor.execute(flow, executionContext, flowPayload);
				return null;
			}
		};
		executionTask.setOnSucceeded(event -> {
			executionContext.getPayloadPublishSubject().onCompleted();
			
			runButton.disableProperty().unbind();
			stopButton.disableProperty().unbind();

			runButton.setDisable(false);
			stopButton.setDisable(true);
		});
		executionTask.setOnFailed(event -> {
			executionContext.getPayloadPublishSubject().onError(executionTask.getException());

			runButton.disableProperty().unbind();
			stopButton.disableProperty().unbind();

			runButton.setDisable(false);
			stopButton.setDisable(true);
		});
		executionTask.setOnCancelled(event -> {
			log.info("UI: got event about execution cancelling");

			runButton.disableProperty().unbind();
			stopButton.disableProperty().unbind();

			runButton.setDisable(false);
			stopButton.setDisable(true);
		});

		executor.submit(executionTask);

		runButton.disableProperty().bind(executionTask.runningProperty());
		stopButton.disableProperty().bind(Bindings.not(executionTask.runningProperty()));
	}

	@FXML
	private void onStopBtnClicked() {
		executionTask.cancel(true);
	}
}
