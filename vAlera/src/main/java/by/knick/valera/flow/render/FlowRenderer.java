package by.knick.valera.flow.render;

import org.springframework.stereotype.Component;

import by.knick.valera.flow.Flow;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

@Component
public class FlowRenderer extends StepRenderer<Flow>{
	@Override
	public String renderToString(Flow step) {
		return "Project";
	}

	@Override
	public Parent renderToDetailsWidget(Flow step) {
		return new StackPane(new Text("I'm "+getClass().getSimpleName()));
	}

	@Override
	public Class<Flow> getCaredStepClass() {
		return Flow.class;
	}
}
