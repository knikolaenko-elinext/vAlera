package by.knick.valera.ui;

import java.net.URL;
import java.util.ResourceBundle;

import by.knick.valera.utils.JsonUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

public class ConfigPreviewWidget implements Initializable {
	@FXML
	private TextArea configTextArea;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
	
	@FXML
	public void onUpdateBtnClicked(){
		configTextArea.setText(JsonUtils.writePretty(DashboardController.getInstance().getProject().getConfig()));
	}
}
