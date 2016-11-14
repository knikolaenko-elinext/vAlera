package by.knick.valera.flow.exec;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import by.knick.valera.flow.PaginationStep;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PaginationStepExecutor extends CompositeStepExecutor<PaginationStep> {

	@Override
	public Class<PaginationStep> getCaredStepClass() {
		return PaginationStep.class;
	}

	@SuppressWarnings("unchecked")
	@SneakyThrows
	@Override
	public void execute(PaginationStep step, ExecutionContext executionContext, Map<String, Object> payload) {
		int pageIdx = 0;

		boolean isThisStepPayloadPublisher = isThisStepPayloadPublisher(executionContext, step);
		if (!isThisStepPayloadPublisher) {
			payload.put("pages", new ArrayList<>());
		}

		while (true) {
			if (Thread.interrupted()) {
				throw new InterruptedException("Flow execution was interrupted by the user");
			}

			pageIdx++;
			if (pageIdx > step.getPagesCountLimit()) {
				break;
			}

			Element currentElement = executionContext.getCurrentElement();
			if (pageIdx > 1) {
				Elements nextPageLinkEls = currentElement.select(step.getNextPageSelector());
				if (nextPageLinkEls.isEmpty()) {
					break;
				}
				String nextPageUrl = nextPageLinkEls.get(0).absUrl("href");
				if (nextPageUrl.isEmpty()) {
					break;
				}

				currentElement = Jsoup.connect(nextPageUrl).userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.90 Safari/537.36").get();
				executionContext.setCurrentElement(currentElement);
			}

			Map<String, Object> thisPagePayload = new LinkedHashMap<>();

			log.info("Started page #{}", pageIdx);
			executeChildSteps(step, executionContext, thisPagePayload);
			log.info("Finished page #{}", pageIdx);

			if (isThisStepPayloadPublisher) {
				publishPayload(executionContext, thisPagePayload);
			} else {
				((List<Object>) payload.get("pages")).add(thisPagePayload);
			}
		}
	}
}
