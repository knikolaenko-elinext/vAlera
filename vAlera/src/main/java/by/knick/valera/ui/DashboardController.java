package by.knick.valera.ui;

import java.net.URL;
import java.util.ResourceBundle;

import by.knick.valera.project.Config;
import by.knick.valera.project.Project;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import lombok.Getter;

public class DashboardController implements Initializable {
	private static DashboardController INSTANSE;

	@FXML
	@Getter
	private BrowserController browserController;
	@FXML
	@Getter
	private WorkflowEditorController workflowEditorController;
	@FXML
	@Getter
	private PayloadPreviewWidget payloadPreviewWidgetController;
	@FXML
	@Getter
	private ConfigPreviewWidget configPreviewWidgetController;

	@Getter
	private Project project = new Project();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		INSTANSE = this;
	}

	public static DashboardController getInstance() {
		return INSTANSE;
	}
	
	@FXML
	private void onPayloadTabSelected(){
		// payloadPreviewWidgetController.onUpdateBtnClicked();
	}
	
	@FXML
	private void onConfigTabSelected(){
		configPreviewWidgetController.onUpdateBtnClicked();
	}

	public void openNewProject(Config config) {
		this.project = new Project(config);
		this.workflowEditorController.refillTreeView();
	}
}
