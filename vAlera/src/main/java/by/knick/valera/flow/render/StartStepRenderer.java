package by.knick.valera.flow.render;

import org.springframework.stereotype.Component;

import by.knick.valera.flow.StartStep;
import javafx.scene.Parent;

@Component
public class StartStepRenderer extends StepRenderer<StartStep> {
	@Override
	public String renderToString(StartStep step) {
		return String.format("Start: %s (%s)", step.getName(), step.getStartUrl());
	}

	@Override
	public Parent renderToDetailsWidget(StartStep step) {
		return StartStepWidget.build(step);
	}

	@Override
	public Class<StartStep> getCaredStepClass() {
		return StartStep.class;
	}
}
