package by.knick.valera.flow.exec;

import java.util.Map;

import org.jsoup.nodes.Element;

import by.knick.valera.flow.Step;
import lombok.Data;
import rx.subjects.PublishSubject;

@Data
public class ExecutionContext{	
	private Element currentElement;
	private Step payloadPubliser;
	private PublishSubject<Map<String, Object>> payloadPublishSubject;
}
