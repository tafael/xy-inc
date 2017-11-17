package com.prova.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.prova.model.Field;
import com.prova.model.Model;

public class ResultExtractor {
	
	public static Map<String, Object> extractResult(Model model, Object[] _result) {
		if (_result == null) return null;
		Map<String, Object> result = new HashMap<String, Object>();
		int i = 0;
		for (Field field : model.getFields()) {
			result.put(field.getName(), _result[i++]);
		}
		return result;
	}

	public static List<Map<String, Object>> extractResult(Model model, List<Object[]> _results) {
		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		for (Object[] _result : _results) {
			Map<String, Object> result = extractResult(model, _result);
			results.add(result);
		}
		return results;
	}
	
	public static Map<String, Object> setGeneratedId(Model model, Map<String, Object> entity, Object _result) {
		if (_result == null) return entity;
		entity.put(model.getTableId().getName(), _result);
		return entity;
	}

	public static List<Map<String, Object>> setGeneratedId(Model model, List<Map<String, Object>> entities, Object[] _result) {
		if (_result == null) return entities;
		int i = 0;
		for (Map<String, Object> entity : entities) {
			setGeneratedId(model, entity, _result[i++]);
		}
		return entities;
	}

}
