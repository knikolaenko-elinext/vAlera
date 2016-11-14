package by.knick.valera.flow.exec;

import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import by.knick.valera.flow.DigStep;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DigStepExecutor extends CompositeStepExecutor<DigStep> {

	@Override
	public Class<DigStep> getCaredStepClass() {
		return DigStep.class;
	}

	@SneakyThrows
	@Override
	public void execute(DigStep step, ExecutionContext executionContext, Map<String, Object> payload) {
		log.info("Start digging into: {}", step);

		boolean isThisStepPayloadPublisher = isThisStepPayloadPublisher(executionContext, step);

		Element currentElement = executionContext.getCurrentElement();

		Elements digLinkEls = currentElement.select(step.getDigLinkSelector());
		if (!digLinkEls.isEmpty()) {
			String digUrl = digLinkEls.get(0).absUrl("href");
			if (!digUrl.isEmpty()) {
				Document document = Jsoup.connect(digUrl).userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.90 Safari/537.36").get();
				executionContext.setCurrentElement(document);

				executeChildSteps(step, executionContext, payload);

				if (isThisStepPayloadPublisher) {
					publishPayload(executionContext, payload);
				}

				executionContext.setCurrentElement(currentElement);
			}
		}
		log.info("Finished digging");
	}
}
