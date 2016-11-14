package by.knick.valera.flow.render;

import java.net.URL;
import java.util.ResourceBundle;

import org.w3c.dom.html.HTMLElement;

import by.knick.valera.flow.ListStep;
import by.knick.valera.ui.DashboardController;
import by.knick.valera.ui.UiStrings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import lombok.Getter;
import lombok.SneakyThrows;

public class ListStepConfigWidget implements Initializable {
	@FXML
	private HtmlElementCssSelectorField listElementCssSelectorController;

	@FXML
	private HtmlElementPickerField firstElementSelectorController;

	@FXML
	private HtmlElementPickerField secondElementSelectorController;

	@Getter
	private final ObjectProperty<ListStep> valueProperty = new SimpleObjectProperty<>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ChangeListener<? super HTMLElement> listener = (obs, oldVal, newVal) -> {
			HTMLElement first = firstElementSelectorController.getValueProperty().get();
			HTMLElement second = secondElementSelectorController.getValueProperty().get();

			if (first == null || second == null) {
				return;
			}
			HTMLElement commonParent = null;
			findCommonParent: for (commonParent = first; commonParent.getParentNode() instanceof HTMLElement; commonParent = (HTMLElement) commonParent.getParentNode()) {
				for (HTMLElement currentSecondParent = second; currentSecondParent
						.getParentNode() instanceof HTMLElement; currentSecondParent = (HTMLElement) currentSecondParent.getParentNode()) {
					if (commonParent == currentSecondParent) {
						break findCommonParent;
					}
				}
			}

			HTMLElement firstListItem = first;
			while (true) {
				if (firstListItem.getParentNode() == commonParent) {
					break;
				}
				firstListItem = (HTMLElement) firstListItem.getParentNode();
			}

			listElementCssSelectorController.setFromHtmlElement(firstListItem);
		};
		firstElementSelectorController.getValueProperty().addListener(listener);
		secondElementSelectorController.getValueProperty().addListener(listener);

		valueProperty.addListener((obs, oldVal, newVal) -> {
			if (newVal == null) {
				listElementCssSelectorController.getValueProperty().set("");
				return;
			}
			listElementCssSelectorController.getValueProperty().set(newVal.getSelector());
		});
		listElementCssSelectorController.getValueProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal == null || newVal.isEmpty()) {
				valueProperty.getValue().setSelector(null);
				return;
			}
			valueProperty.getValue().setSelector(newVal);
			DashboardController.getInstance().getWorkflowEditorController().refreshTreeView();
		});
	}

	public void setStep(ListStep step) {
		valueProperty.set(step);
	}

	@SneakyThrows
	public static Parent build(ListStep listStep) {
		URL fxml = ListStepConfigWidget.class.getResource("ListStepConfigWidget.fxml");
		ResourceBundle resources = UiStrings.getInstance().getBundle();
		FXMLLoader fxmlLoader = new FXMLLoader(fxml, resources);
		Parent view = fxmlLoader.load();
		ListStepConfigWidget controller = fxmlLoader.getController();
		controller.setStep(listStep);
		return view;
	}
}
