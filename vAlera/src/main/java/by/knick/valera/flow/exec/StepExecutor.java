package by.knick.valera.flow.exec;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import by.knick.valera.flow.Step;

public abstract class StepExecutor<T extends Step> {
	@Autowired
	protected StepExecutorsRegistry executorsRegistry;
	
	public abstract Class<T> getCaredStepClass();
	
	public abstract void execute(T step, ExecutionContext executionContext, Map<String,Object> payload);
}
