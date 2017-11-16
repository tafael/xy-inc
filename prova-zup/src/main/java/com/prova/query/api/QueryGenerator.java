package com.prova.query.api;

import java.util.List;
import java.util.Map;

import com.prova.model.Model;

public interface QueryGenerator {
	
	List<String> generateCreateTable(Model table);
	
	List<String> generateAlterTable(Model _old, Model _new);
	
	String generateSelect(Model table, List<Map<String, Object>> params);

	String generateInsert(Model table, List<Map<String, Object>> entities);

	String generateUpdate(Model table, List<Map<String, Object>> entities);

	String generateDelete(Model table, List<Map<String, Object>> entities);

}
