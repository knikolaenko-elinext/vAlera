package by.knick.valera.utils;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import lombok.SneakyThrows;

public class JsonUtils {
	private JsonUtils() {
	}
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final ObjectWriter PRETTY_WRITER;
	static {
		PRETTY_WRITER = MAPPER.writer(new DefaultPrettyPrinter());
	}
	
	@SneakyThrows
	public static String writePretty(Object bean){
		return PRETTY_WRITER.writeValueAsString(bean);
	}
	
	@SneakyThrows
	public static String write(Object bean){
		return MAPPER.writeValueAsString(bean);
	}
	
	@SneakyThrows
	public static <T> T read(String str, Class<T> clz){
		return MAPPER.readValue(str, clz);
	}
}
