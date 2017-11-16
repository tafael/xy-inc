package com.prova.query.api;

import com.prova.query.PostgreSQLQueryGenerator;

public class QueryGeneratorFactory {
	
	public static QueryGenerator getQueryGenerator() {
		return new PostgreSQLQueryGenerator();
	}

}
