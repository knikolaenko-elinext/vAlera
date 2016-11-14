package by.knick.valera.flow.render;

import java.net.URL;
import java.util.ResourceBundle;

import by.knick.valera.flow.ScrapTextStep;
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

public class ScrapTextStepWidget implements Initializable {
	@FXML
	private TextField payloadKeyTextField;
	@FXML
	private HtmlElementCssSelectorPickerField cssSelectorPickerController;

	@Getter
	private final ObjectProperty<ScrapTextStep> valueProperty = new SimpleObjectProperty<>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		valueProperty.addListener((obs, oldVal, newVal) -> {
			if (newVal == null) {
				payloadKeyTextField.textProperty().set("");
				cssSelectorPickerController.getValueProperty().set("");
				return;
			}
			payloadKeyTextField.textProperty().set(newVal.getPayloadKey());
			cssSelectorPickerController.getValueProperty().set(newVal.getSelector());
		});
		cssSelectorPickerController.getValueProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal == null || newVal.isEmpty()) {
				valueProperty.getValue().setSelector(null);
				return;
			}
			valueProperty.getValue().setSelector(newVal);
			DashboardController.getInstance().getWorkflowEditorController().refreshTreeView();
		});
		payloadKeyTextField.textProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal == null || newVal.isEmpty()) {
				valueProperty.getValue().setPayloadKey(null);
				return;
			}
			valueProperty.getValue().setPayloadKey(newVal);
			DashboardController.getInstance().getWorkflowEditorController().refreshTreeView();
		});
	}

	@SneakyThrows
	public static Parent build(ScrapTextStep step) {
		URL fxml = ScrapTextStepWidget.class.getResource("ScrapTextStepWidget.fxml");
		ResourceBundle resources = UiStrings.getInstance().getBundle();
		FXMLLoader fxmlLoader = new FXMLLoader(fxml, resources);
		Parent view = fxmlLoader.load();
		ScrapTextStepWidget controller = fxmlLoader.getController();
		controller.getValueProperty().set(step);
		return view;
	}
}
