package by.knick.valera.flow.exec;

import java.util.Map;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import by.knick.valera.flow.ScrapTextStep;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ScrapTextStepExecutor extends StepExecutor<ScrapTextStep>{

	@Override
	public void execute(ScrapTextStep step, ExecutionContext executionContext, Map<String,Object> payload) {
		log.info("Start text scrapping: {}", step);
		Element currentElement = executionContext.getCurrentElement();
		Elements targetElements = currentElement.select(step.getSelector());
		String text = targetElements.text();		
		payload.put(step.getPayloadKey(), text);
		log.info("Finished text scrapping: {}", text);
	}

	@Override
	public Class<ScrapTextStep> getCaredStepClass() {
		return ScrapTextStep.class;
	}
}
