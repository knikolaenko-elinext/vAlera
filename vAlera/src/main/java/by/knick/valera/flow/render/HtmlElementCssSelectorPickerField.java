package by.knick.valera.flow.render;

import java.net.URL;
import java.util.ResourceBundle;

import org.w3c.dom.html.HTMLElement;

import by.knick.valera.ui.DashboardController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import rx.Observer;

@Slf4j
public class HtmlElementCssSelectorPickerField implements Initializable {
	@FXML
	private TextField selectorTextField;
	@Getter
	private final StringProperty valueProperty = new SimpleStringProperty();
	@Getter
	private final ObjectProperty<HTMLElement> htmlElementProperty = new SimpleObjectProperty<>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		selectorTextField.textProperty().bindBidirectional(valueProperty);

		htmlElementProperty.addListener((obs, oldVal, newVal) -> {
			valueProperty.set(RenderUtils.htmlElToString(newVal));
		});
	}

	@Setter
	private Class<? extends HTMLElement> stickToClazz;

	@FXML
	private void onPickBtnClicked() {
		Observable<HTMLElement> observable = DashboardController.getInstance().getBrowserController().startHoverMode();
		observable.subscribe(new Observer<HTMLElement>() {
			private HTMLElement last = null;

			@Override
			public void onNext(HTMLElement htmlElement) {
				last = htmlElement;
				log.trace("Hovered {}", htmlElement);
			}

			@Override
			public void onCompleted() {
				log.debug("Last Hovered {}", last);
				HTMLElement pickedElement = last;
				if (stickToClazz != null) {
					while (true) {
						if (stickToClazz.isInstance(pickedElement)) {
							// Bingo!
							break;
						}
						if (!(pickedElement.getParentNode() instanceof HTMLElement)) {
							// Didn't found - end with initial
							pickedElement = last;
							break;
						}
						pickedElement = (HTMLElement) pickedElement.getParentNode();
					}
				}
				log.debug("Picked {}", pickedElement);
				htmlElementProperty.setValue(pickedElement);
			}

			@Override
			public void onError(Throwable e) {
				htmlElementProperty.setValue(null);
			}
		});
	}
}
