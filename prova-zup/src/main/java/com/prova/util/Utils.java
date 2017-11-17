package com.prova.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import com.prova.model.Field;
import com.prova.model.Model;
import com.prova.model.TypeEnum;

public final class Utils {
	
	private Utils() {}

	public static boolean isEmpty(String value) {
		return value == null || value.trim().isEmpty();
	}
	
	public static boolean isValidSQLName(String name) {
		if (name == null)
			return false;
		return name.matches("[0-9a-zA-Z_]+");
	}
	
	public static String getRequestParameter(String key) {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
	}
	
	private static Date toDate(String string) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = dateFormat.parse(string);
			return date;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static void convertDateTypes(Model model, List<Map<String, Object>> data) {
		for (Map<String, Object> _data : data) {
			convertDateTypes(model, _data);
		}
	}
	
	public static void convertDateTypes(Model model, Map<String, Object> data) {
		for (Field field : model.getFields()) {
			if (field.getType() == TypeEnum.DATE || field.getType() == TypeEnum.TIMESTAMP) {
				Object fieldValue = data.get(field.getName());
				if (fieldValue != null) {
					if (fieldValue instanceof String) {
						data.put(field.getName(), toDate((String) fieldValue));
					} else if (fieldValue instanceof Number) {
						Number number = (Number) fieldValue;
						data.put(field.getName(), new Date(number.longValue()));
					}
				}
			}
		}
	}
	
}
