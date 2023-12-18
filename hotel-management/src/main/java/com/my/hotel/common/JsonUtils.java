package com.my.hotel.common;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class JsonUtils {

	private JsonUtils() {
	}

	public static ObjectMapper getObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		return objectMapper;
	}

	public static String convertToString(Object o) {
		String result = "";
		final ObjectMapper objectMapper = getObjectMapper();
		try {
			result = objectMapper.writeValueAsString(o);
		} catch (Exception e) {
			log.error("Error convertToString " + o.getClass().getName() + " object to JSON, " + e);
		}
		return result;
	}

	public static <T> T convertToObject(String jsonString, Class<T> clazz) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return (T) mapper.readValue(jsonString, clazz);
		} catch (Exception e) {
			log.error("Error convertToObject {}", e);
			return null;
		}
	}
}
