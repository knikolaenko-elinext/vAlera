package by.knick.valera.flow.render;

import org.springframework.stereotype.Component;

import by.knick.valera.flow.DigStep;
import javafx.scene.Parent;

@Component
public class DigStepRenderer extends StepRenderer<DigStep>{

	@Override
	public Class<DigStep> getCaredStepClass() {
		return DigStep.class;
	}

	@Override
	public String renderToString(DigStep step) {
		return String.format("Digging into: %s", step.getDigLinkSelector());
	}

	@Override
	public Parent renderToDetailsWidget(DigStep step) {
		return DigStepWidget.build(step);
	}
}
