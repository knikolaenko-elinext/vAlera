package by.knick.valera.flow.render;

import java.net.URL;
import java.util.ResourceBundle;

import org.w3c.dom.html.HTMLAnchorElement;

import by.knick.valera.flow.DigStep;
import by.knick.valera.ui.DashboardController;
import by.knick.valera.ui.UiStrings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import lombok.Getter;
import lombok.SneakyThrows;

public class DigStepWidget implements Initializable {
	
	@FXML
	private HtmlElementCssSelectorPickerField digLinkSelectorPickerController;
	
	@Getter
	private final ObjectProperty<DigStep> valueProperty = new SimpleObjectProperty<>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		digLinkSelectorPickerController.setStickToClazz(HTMLAnchorElement.class);
		
		valueProperty.addListener((obs, oldVal, newVal) -> {
			if (newVal == null) {
				digLinkSelectorPickerController.getValueProperty().set(null);
				return;
			}
			digLinkSelectorPickerController.getValueProperty().set(newVal.getDigLinkSelector());
		});
		digLinkSelectorPickerController.getValueProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal == null || newVal.isEmpty()) {
				valueProperty.getValue().setDigLinkSelector(null);
				return;
			}
			valueProperty.getValue().setDigLinkSelector(newVal);
			DashboardController.getInstance().getWorkflowEditorController().refreshTreeView();
		});
	}
	
	@SneakyThrows
	public static Parent build(DigStep step) {
		URL fxml = DigStepWidget.class.getResource("DigStepWidget.fxml");
		ResourceBundle resources = UiStrings.getInstance().getBundle();
		FXMLLoader fxmlLoader = new FXMLLoader(fxml, resources);
		Parent view = fxmlLoader.load();
		DigStepWidget controller = fxmlLoader.getController();
		controller.getValueProperty().set(step);
		return view;
	}
}
