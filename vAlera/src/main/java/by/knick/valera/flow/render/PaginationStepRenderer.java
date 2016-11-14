package by.knick.valera.flow.render;

import org.springframework.stereotype.Component;

import by.knick.valera.flow.PaginationStep;
import javafx.scene.Parent;

@Component
public class PaginationStepRenderer extends StepRenderer<PaginationStep> {

	@Override
	public Class<PaginationStep> getCaredStepClass() {
		return PaginationStep.class;
	}

	@Override
	public String renderToString(PaginationStep step) {
		return String.format("Pagination: %s", step.getNextPageSelector());
	}

	@Override
	public Parent renderToDetailsWidget(PaginationStep step) {
		return PaginationStepWidget.build(step);
	}
}
