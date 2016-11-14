package by.knick.valera.ui;

import java.net.URL;
import java.util.ResourceBundle;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLElement;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import rx.Observable;
import rx.subjects.PublishSubject;

public class BrowserController implements Initializable {
	@FXML
	private TextField urlTextField;
	@FXML
	private WebView mainWebView;

	private WebEngine mainWebEngine;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		mainWebEngine = mainWebView.getEngine();

		mainWebEngine.setUserStyleSheetLocation(getClass().getResource("valeraStyle.css").toString());

		mainWebEngine.locationProperty().addListener((observable, oldValue, newValue) -> {
			urlTextField.setText(newValue);
		});

		mainWebEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
			public void changed(ObservableValue<? extends State> observableValue, State oldState, State newState) {
				if (newState == State.SUCCEEDED) {
					setupHovering();
				}
			}
		});
		urlTextField.setOnKeyPressed(keyEvt -> {
			if (keyEvt.getCode() == KeyCode.ENTER) {
				mainWebEngine.load(urlTextField.getText());
			}
		});

		load("http://realt.by/rent/flat-for-long/");
	}

	public void load(String url) {
		mainWebEngine.load(url);
	}

	public String getCurrentUrl() {
		return mainWebEngine.getLocation();
	}

	/* START OF HOVER STUFF */

	private void setupHovering() {
		NodeList allEls = mainWebEngine.getDocument().getElementsByTagName("*");
		for (int i = 0; i < allEls.getLength(); i++) {
			Node el = allEls.item(i);
			((EventTarget) el).addEventListener("mouseenter", mouseEnterListener, false);
			((EventTarget) el).addEventListener("mouseleave", mouseLeaveListener, false);
			((EventTarget) el).addEventListener("click", mouseClickListener, false);
		}
	}

	private boolean isHoveringModeActive = false;
	private HTMLElement currentlyHovered = null;
	private PublishSubject<HTMLElement> hoveredSubject;

	private EventListener mouseEnterListener = new EventListener() {
		public void handleEvent(Event ev) {
			ev.stopPropagation();
			if (!isHoveringModeActive) {
				return;
			}

			EventTarget eventTarget = ev.getTarget();
			HTMLElement htmlElement = (HTMLElement) eventTarget;

			hover(htmlElement);
			hoveredSubject.onNext(htmlElement);
		}
	};

	private EventListener mouseLeaveListener = new EventListener() {
		public void handleEvent(Event ev) {
			ev.stopPropagation();
			if (!isHoveringModeActive) {
				return;
			}

			EventTarget eventTarget = ev.getTarget();
			HTMLElement htmlElement = (HTMLElement) eventTarget;

			unhover(htmlElement);
		}
	};

	private EventListener mouseClickListener = new EventListener() {
		public void handleEvent(Event ev) {
			ev.stopPropagation();
			if (!isHoveringModeActive) {
				return;
			}
			ev.preventDefault();
			stopHoverMode();
		}
	};

	public static final String HOVERED_CLASS = "hovered-by-vAlera";

	private void hover(HTMLElement htmlElement) {
		unhover(this.currentlyHovered);
		if (htmlElement.getClassName() != null) {
			htmlElement.setClassName(htmlElement.getClassName() + " " + HOVERED_CLASS);
		} else {
			htmlElement.setClassName(" " + HOVERED_CLASS);
		}
		this.currentlyHovered = htmlElement;
	}

	private void unhover(HTMLElement htmlElement) {
		if (htmlElement == null)
			return;
		if (htmlElement.getClassName() != null) {
			htmlElement.setClassName(htmlElement.getClassName().replace(" hovered-by-vAlera", ""));
		}
	}

	public Observable<HTMLElement> startHoverMode() {
		hoveredSubject = PublishSubject.create();
		isHoveringModeActive = true;
		return hoveredSubject;
	}

	public void stopHoverMode() {
		isHoveringModeActive = false;
		unhover(currentlyHovered);
		hoveredSubject.onCompleted();
		hoveredSubject = null;
	}

	/* END OF HOVER STUFF */
}