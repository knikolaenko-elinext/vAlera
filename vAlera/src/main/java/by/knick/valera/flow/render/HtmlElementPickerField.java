package by.knick.valera.flow.render;

import java.net.URL;
import java.util.ResourceBundle;

import org.w3c.dom.html.HTMLElement;

import by.knick.valera.ui.DashboardController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import rx.Observer;

@Slf4j
public class HtmlElementPickerField implements Initializable {
	@FXML
	private TextField selectorTextField;

	@Getter
	private final ObjectProperty<HTMLElement> valueProperty = new SimpleObjectProperty<>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		valueProperty.addListener((obs, oldVal, newVal) -> {
			selectorTextField.setText(RenderUtils.htmlElToString(newVal));
		});
	}

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
				log.debug("Picked {}", last);
				valueProperty.setValue(last);
			}

			@Override
			public void onError(Throwable e) {
				valueProperty.setValue(null);
			}
		});
	}
}
