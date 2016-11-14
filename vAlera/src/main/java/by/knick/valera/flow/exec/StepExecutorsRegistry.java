package by.knick.valera.flow.exec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import by.knick.valera.flow.Step;

@Component
public class StepExecutorsRegistry {
	private Map<Class<?>, StepExecutor<?>> executors = new HashMap<>();

	@Autowired
	protected void init(List<StepExecutor<?>> executors) {
		for (StepExecutor<?> executor : executors) {
			this.executors.put(executor.getCaredStepClass(), executor);
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends Step> StepExecutor<Step> getExecutor(T step) {
		return (StepExecutor<Step>) executors.get(step.getClass());
	}

	private static StepExecutorsRegistry INSTANCE;

	@PostConstruct
	protected void postInit() {
		INSTANCE = this;
	}

	public static StepExecutorsRegistry getInstance() {
		return INSTANCE;
	}
}
