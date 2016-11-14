package by.knick.valera.flow.render;

import org.springframework.stereotype.Component;

import by.knick.valera.flow.ScrapTextStep;
import javafx.scene.Parent;

@Component
public class ScrapTextStepRenderer extends StepRenderer<ScrapTextStep>{

	@Override
	public String renderToString(ScrapTextStep step) {
		return String.format("Scrap: %s", step.getPayloadKey());
	}

	@Override
	public Parent renderToDetailsWidget(ScrapTextStep step) {
		return ScrapTextStepWidget.build(step);
	}

	@Override
	public Class<ScrapTextStep> getCaredStepClass() {
		return ScrapTextStep.class;
	}
}
