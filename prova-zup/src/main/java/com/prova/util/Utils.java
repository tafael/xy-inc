package com.prova.util;

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
	
}
