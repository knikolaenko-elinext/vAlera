package by.knick.valera.flow.render;

import org.springframework.stereotype.Component;

import by.knick.valera.flow.ListStep;
import javafx.scene.Parent;

@Component
public class ListStepRenderer extends StepRenderer<ListStep>{
	@Override
	public String renderToString(ListStep step) {
		return "List: " + step.getSelector();
	}

	@Override
	public Parent renderToDetailsWidget(ListStep step) {
		return ListStepConfigWidget.build(step);
	}

	@Override
	public Class<ListStep> getCaredStepClass() {
		return ListStep.class;
	}
}