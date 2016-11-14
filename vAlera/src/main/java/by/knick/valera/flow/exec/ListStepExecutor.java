package by.knick.valera.flow.exec;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import by.knick.valera.flow.ListStep;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ListStepExecutor extends CompositeStepExecutor<ListStep> {
	@SuppressWarnings("unchecked")
	@SneakyThrows
	@Override
	public void execute(ListStep step, ExecutionContext executionContext, Map<String, Object> payload) {
		log.info("Start extracting list: {}", step);
		Element currentElement = executionContext.getCurrentElement();

		boolean isThisStepPayloadPublisher = isThisStepPayloadPublisher(executionContext, step);
		if (!isThisStepPayloadPublisher) {
			payload.put("items", new ArrayList<>());
		}

		Elements targetElements = currentElement.select(step.getSelector());
		log.info("... extracting list: extracted elements: {}", targetElements.size());

		for (int i = 0; i < targetElements.size(); i++) {
			if (Thread.interrupted()) {
				throw new InterruptedException("Flow execution was interrupted by the user");
			}

			Element targetElement = targetElements.get(i);
			executionContext.setCurrentElement(targetElement);

			Map<String, Object> thisListItemPayload = new LinkedHashMap<>();
			executeChildSteps(step, executionContext, thisListItemPayload);

			if (isThisStepPayloadPublisher) {
				publishPayload(executionContext, thisListItemPayload);
			} else {
				((List<Object>) payload.get("items")).add(thisListItemPayload);
			}
		}
		executionContext.setCurrentElement(currentElement);

		log.info("Finished extracting list elements.");
	}

	@Override
	public Class<ListStep> getCaredStepClass() {
		return ListStep.class;
	}
}
