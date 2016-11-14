package by.knick.valera.flow.render;

import java.net.URL;
import java.util.ResourceBundle;

import org.w3c.dom.html.HTMLElement;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import lombok.Getter;

public class HtmlElementCssSelectorField implements Initializable {

	@FXML
	private TextField selectorTextField;

	@Getter
	private final StringProperty valueProperty = new SimpleStringProperty();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		selectorTextField.textProperty().bindBidirectional(valueProperty);
	}

	public void setFromHtmlElement(HTMLElement firstListItem) {
		valueProperty.set(RenderUtils.htmlElToString(firstListItem));
	}
}
