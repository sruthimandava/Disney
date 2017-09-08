package com.disney.studios.api.challenge.biz;

import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;

public class Converter {

	public static String getJSONString(Map<String, String> properties) {
		JSONObject jsonObject = new JSONObject();
		if (properties != null) {
			Set<String> keys = properties.keySet();
			for (String key : keys) {
				String value = properties.get(key);
				jsonObject.put(key, value);
			}
		}
		return jsonObject.toJSONString();
	}

}
