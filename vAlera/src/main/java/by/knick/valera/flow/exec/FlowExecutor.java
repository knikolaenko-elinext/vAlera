package by.knick.valera.flow.exec;

import java.util.Map;

import org.springframework.stereotype.Component;

import by.knick.valera.flow.CompositeStep;
import by.knick.valera.flow.Flow;
import by.knick.valera.flow.Step;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FlowExecutor extends CompositeStepExecutor<Flow> {

	@Override
	public Class<Flow> getCaredStepClass() {
		return Flow.class;
	}

	@Override
	public void execute(Flow step, ExecutionContext executionContext, Map<String,Object> payload) {
		log.info("Going to execute flow...");
		for (Step childStep: step.getChildSteps()){
			Step payloadPublisher = determainePayloadPubliserStepForThisBranch(childStep);
			log.debug("Payload publisher is {}", payloadPublisher);
			executionContext.setPayloadPubliser(payloadPublisher);
			StepExecutor<Step> executor = executorsRegistry.getExecutor(childStep);
			executor.execute(childStep, executionContext, payload);
		}
		log.info("Finished flow execution");
	}

	private Step determainePayloadPubliserStepForThisBranch(Step startStep) {
		Step currentCandidate = startStep;
		while (true){
			if (!(currentCandidate instanceof CompositeStep)){
				break;
			}
			CompositeStep currentCandidateAsCompositeStep = (CompositeStep) currentCandidate;
			if (currentCandidateAsCompositeStep.getChildSteps().size() != 1){
				break;
			}
			Step nestedStep = currentCandidateAsCompositeStep.getChildSteps().get(0);
			if (!(nestedStep instanceof CompositeStep)){
				break;
			}
			currentCandidate = nestedStep;
		}
		return currentCandidate;
	}
}
