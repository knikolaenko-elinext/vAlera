package by.knick.valera.flow.exec;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import by.knick.valera.flow.StartStep;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StartStepExecutor extends CompositeStepExecutor<StartStep> {

	@Override
	public Class<StartStep> getCaredStepClass() {
		return StartStep.class;
	}

	@SneakyThrows
	@Override
	public void execute(StartStep step, ExecutionContext executionContext, Map<String, Object> payload) {
		log.info("Start loading page: {}", step);

		boolean isThisStepPayloadPublisher = isThisStepPayloadPublisher(executionContext, step);

		Document document = Jsoup.connect(step.getStartUrl()).userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.90 Safari/537.36").get();
		executionContext.setCurrentElement(document);

		log.info("Finished loading page: {} bytes", document.toString().length());

		Map<String, Object> thisSitePayload = new LinkedHashMap<>();
		executeChildSteps(step, executionContext, thisSitePayload);

		if (isThisStepPayloadPublisher) {
			publishPayload(executionContext, thisSitePayload);
		} else {
			payload.put(step.getName(), thisSitePayload);
		}
	}
}
