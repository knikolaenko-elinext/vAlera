package by.knick.valera.flow.render;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import by.knick.valera.flow.Step;

@Component
public class StepRenderersRegistry {
	private Map<Class<?>, StepRenderer<?>> renderers = new HashMap<>();

	@Autowired
	protected void init(List<StepRenderer<?>> renderers) {
		for (StepRenderer<?> renderer : renderers) {
			this.renderers.put(renderer.getCaredStepClass(), renderer);
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends Step> StepRenderer<Step> getRenderer(T step) {
		return (StepRenderer<Step>) renderers.get(step.getClass());
	}

	private static StepRenderersRegistry INSTANCE;

	@PostConstruct
	protected void postInit() {
		INSTANCE = this;
	}

	public static StepRenderersRegistry getInstance() {
		return INSTANCE;
	}
}
