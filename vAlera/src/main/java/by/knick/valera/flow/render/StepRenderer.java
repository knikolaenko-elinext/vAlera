package by.knick.valera.flow.render;

import by.knick.valera.flow.Step;
import javafx.scene.Parent;

public abstract class StepRenderer<T extends Step> {
	public abstract Class<T> getCaredStepClass();
	
	public abstract String renderToString(T step);
	
	public abstract Parent renderToDetailsWidget(T step);
}
