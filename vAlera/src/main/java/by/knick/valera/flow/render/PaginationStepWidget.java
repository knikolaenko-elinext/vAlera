package by.knick.valera.flow.render;

import java.net.URL;
import java.util.ResourceBundle;

import org.w3c.dom.html.HTMLAnchorElement;

import by.knick.valera.flow.PaginationStep;
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

public class PaginationStepWidget implements Initializable {
	
	@FXML
	private HtmlElementCssSelectorPickerField nextPageLinkSelectorPickerController;
	@FXML
	private TextField pagesCountLimitField;
	
	@Getter
	private final ObjectProperty<PaginationStep> valueProperty = new SimpleObjectProperty<>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nextPageLinkSelectorPickerController.setStickToClazz(HTMLAnchorElement.class);
		
		valueProperty.addListener((obs, oldVal, newVal) -> {
			if (newVal == null) {
				nextPageLinkSelectorPickerController.getValueProperty().set(null);
				pagesCountLimitField.textProperty().set(null);
				return;
			}
			nextPageLinkSelectorPickerController.getValueProperty().set(newVal.getNextPageSelector());
			pagesCountLimitField.textProperty().set(String.valueOf(newVal.getPagesCountLimit()));
		});
		nextPageLinkSelectorPickerController.getValueProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal == null || newVal.isEmpty()) {
				valueProperty.getValue().setNextPageSelector(null);
				return;
			}
			valueProperty.getValue().setNextPageSelector(newVal);
			DashboardController.getInstance().getWorkflowEditorController().refreshTreeView();
		});
		pagesCountLimitField.textProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal == null || newVal.isEmpty()) {
				valueProperty.getValue().setPagesCountLimit(1);
				return;
			}
			valueProperty.getValue().setPagesCountLimit(Integer.valueOf(newVal));
			DashboardController.getInstance().getWorkflowEditorController().refreshTreeView();
		});
	}
	
	@SneakyThrows
	public static Parent build(PaginationStep step) {
		URL fxml = PaginationStepWidget.class.getResource("PaginationStepWidget.fxml");
		ResourceBundle resources = UiStrings.getInstance().getBundle();
		FXMLLoader fxmlLoader = new FXMLLoader(fxml, resources);
		Parent view = fxmlLoader.load();
		PaginationStepWidget controller = fxmlLoader.getController();
		controller.getValueProperty().set(step);
		return view;
	}
}
