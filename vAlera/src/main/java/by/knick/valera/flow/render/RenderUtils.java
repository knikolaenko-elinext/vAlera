package by.knick.valera.flow.render;

import java.util.Arrays;
import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLElement;

import by.knick.valera.ui.BrowserController;

public class RenderUtils {
	private RenderUtils() {
	}

	private static final List<String> STANDARD_ATTRS = Arrays.asList("class", "id", "href", "style", "target", "src");

	public static String htmlElToString(HTMLElement el) {
		if (el == null) {
			return "";
		}
		String result = "";
		int totalScore = 0;

		String elSelector = onlyThisToString(el);
		int elSelectorScore = estimateSelectorScore(elSelector);

		result += elSelector;
		totalScore += elSelectorScore;

		while (totalScore < 5) {
			if (!(el.getParentNode() instanceof HTMLElement)) {
				break;
			}
			el = (HTMLElement) el.getParentNode();

			elSelector = onlyThisToString(el);
			elSelectorScore = estimateSelectorScore(elSelector);

			result = elSelector + " > " + result;
			totalScore += elSelectorScore;

		}
		return result;
	}

	private static String onlyThisToString(HTMLElement el) {
		StringBuilder text = new StringBuilder();
		text.append(el.getNodeName().toLowerCase());
		if (el.getId() != null) {
			text.append("#").append(el.getId());
		}
		if (el.getClassName() != null && !el.getClassName().isEmpty()) {
			String[] classes = el.getClassName().split("\\s");
			for (String cssClass : classes) {
				if (cssClass.isEmpty() || cssClass.equals(BrowserController.HOVERED_CLASS)) {
					continue;
				}
				text.append(".").append(cssClass);
			}
		}
		NamedNodeMap attributes = el.getAttributes();
		for (int i = 0; i < attributes.getLength(); i++) {
			Node attr = attributes.item(i);
			String name = attr.getNodeName();
			if (STANDARD_ATTRS.contains(name)) {
				continue;
			}
			String value = attr.getNodeValue();
			text.append("[").append(name).append("=").append(value).append("]");
		}
		return text.toString();
	}

	private static int estimateSelectorScore(String elSelector) {
		int score = 1;
		if (elSelector.indexOf(".") > -1) {
			score+=2;
		}
		if (elSelector.indexOf("[") > -1) {
			score+=2;
		}
		return score;
	}
}
