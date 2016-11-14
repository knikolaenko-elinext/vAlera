package by.knick.valera.flow.render;

import java.net.URL;
import java.util.ResourceBundle;

import by.knick.valera.flow.StartStep;
import by.knick.valera.ui.DashboardController;
import by.knick.valera.ui.UiStrings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.SneakyThrows;

public class StartStepWidget implements Initializable {
	@FXML
	private TextField nameTextField;
	@FXML
	private TextField urlTextField;

	@Getter
	private final ObjectProperty<StartStep> valueProperty = new SimpleObjectProperty<>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		valueProperty.addListener((obs, oldVal, newVal) -> {
			if (newVal == null) {
				nameTextField.textProperty().set("");
				urlTextField.textProperty().set("");
				return;
			}
			nameTextField.textProperty().set(newVal.getName());
			urlTextField.textProperty().set(newVal.getStartUrl());
		});
		nameTextField.textProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal == null || newVal.isEmpty()) {
				valueProperty.getValue().setName(null);
				return;
			}
			valueProperty.getValue().setName(newVal);
			DashboardController.getInstance().getWorkflowEditorController().refreshTreeView();
		});
		urlTextField.textProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal == null || newVal.isEmpty()) {
				valueProperty.getValue().setStartUrl(null);
				return;
			}
			valueProperty.getValue().setStartUrl(newVal);
			DashboardController.getInstance().getWorkflowEditorController().refreshTreeView();
		});
	}

	@FXML
	protected void onOpenBtnClicked() {
		DashboardController.getInstance().getBrowserController().load(urlTextField.getText());
	}

	@SneakyThrows
	public static Parent build(StartStep step) {
		URL fxml = ListStepConfigWidget.class.getResource("StartStepWidget.fxml");
		ResourceBundle resources = UiStrings.getInstance().getBundle();
		FXMLLoader fxmlLoader = new FXMLLoader(fxml, resources);
		Parent view = fxmlLoader.load();
		StartStepWidget controller = fxmlLoader.getController();
		controller.getValueProperty().set(step);
		return view;
	}
}
