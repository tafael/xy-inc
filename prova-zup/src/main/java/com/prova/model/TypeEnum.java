package com.prova.model;

public enum TypeEnum {
	
	INTEGER("integer"),
	CHARACTER("character varying"),
	TIMESTAMP("timestamp"),
	DATE("date"),
	DECIMAL("decimal");
	
	public final String sqlStatement;
	
	private TypeEnum(String sqlStatement) {
		this.sqlStatement = sqlStatement;
	}
}
