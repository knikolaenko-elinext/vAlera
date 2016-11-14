package by.knick.valera.flow.exec;

import java.util.Map;

import by.knick.valera.flow.CompositeStep;
import by.knick.valera.flow.Step;
import rx.subjects.PublishSubject;

public abstract class CompositeStepExecutor<T extends CompositeStep> extends StepExecutor<T>{

	protected void executeChildSteps(T step, ExecutionContext executionContext, Map<String,Object> payload) {
		for (Step childStep: step.getChildSteps()){
			StepExecutor<Step> executor = executorsRegistry.getExecutor(childStep);
			executor.execute(childStep, executionContext, payload);
		}
	}
	
	protected static void publishPayload(ExecutionContext executionContext, Map<String, Object> payload){
		PublishSubject<Map<String, Object>> publishSubject = executionContext.getPayloadPublishSubject();
		publishSubject.onNext(payload);
	}
	
	protected static boolean isThisStepPayloadPublisher(ExecutionContext executionContext, Step step) {
		boolean isThisStepPayloadPublisher = executionContext.getPayloadPubliser() == step;
		return isThisStepPayloadPublisher;
	}
}
