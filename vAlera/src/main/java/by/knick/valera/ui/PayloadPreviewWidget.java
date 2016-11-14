package by.knick.valera.ui;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import by.knick.valera.utils.JsonUtils;
import by.knick.valera.utils.UiUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.schedulers.JavaFxScheduler;

@Slf4j
public class PayloadPreviewWidget implements Initializable {
	@FXML
	private TextArea payloadTextArea;

	private Subscription currentPayloadSubscribtion;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@FXML
	public void onUpdateBtnClicked() {
	}

	public void setPayloadObservable(Observable<Map<String, Object>> payloadObservable) {
		payloadTextArea.clear();

		if (currentPayloadSubscribtion != null) {
			currentPayloadSubscribtion.unsubscribe();
		}

		currentPayloadSubscribtion = payloadObservable.observeOn(JavaFxScheduler.getInstance()).subscribe(new Observer<Map<String, Object>>() {
			@Override
			public void onCompleted() {
				log.debug("Payload Observable: completed");
				currentPayloadSubscribtion.unsubscribe();
			}

			@Override
			public void onError(Throwable e) {
				log.debug("Payload Observable: error");
				UiUtils.displayExceptionAlert(e);
				currentPayloadSubscribtion.unsubscribe();
			}

			@Override
			public void onNext(Map<String, Object> payload) {
				payloadTextArea.appendText("\n");
				payloadTextArea.appendText(JsonUtils.write(payload));
			}
		});
	}
}
