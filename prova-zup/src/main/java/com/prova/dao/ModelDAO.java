package com.prova.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.Dependent;

import org.hibernate.SQLQuery;

import com.prova.model.Model;
import com.prova.query.ResultExtractor;
import com.prova.query.api.QueryGenerator;
import com.prova.query.api.QueryGeneratorFactory;

@Dependent
public class ModelDAO extends GenericDAOImpl<Model, Long> implements GenericDAO<Model, Long> {

	private QueryGenerator queryGenerator = QueryGeneratorFactory.getQueryGenerator();

	public void executeSQL(String sql) {
		openSession().createSQLQuery(sql).executeUpdate();
	}
	
	public void executeSQL(List<String> sqls) {
		for (String sql : sqls) {
			executeSQL(sql);
		}
	}

	public Map<String, Object> findDataById(Model model, Long id) {
		List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
		Map<String, Object> params1 = new HashMap<String, Object>();
		params1.put("id", id);
		params.add(params1);
		String sql = queryGenerator.generateSelect(model, params);
		SQLQuery query = openSession().createSQLQuery(sql);
		return ResultExtractor.extractResult(model, (Object[]) query.uniqueResult());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findAllData(Model model) {
		String sql = queryGenerator.generateSelect(model, null);
		SQLQuery query = openSession().createSQLQuery(sql);
		return ResultExtractor.extractResult(model, query.list());
	}

	public Map<String, Object> insertData(Model model, Map<String, Object> data) {
		List<Map<String, Object>> entities = new ArrayList<Map<String, Object>>();
		entities.add(data);
		String sql = queryGenerator.generateInsert(model, entities);
		SQLQuery query = openSession().createSQLQuery(sql);
		return ResultExtractor.setGeneratedId(model, data, (Object) query.uniqueResult());
	}
	
	public void updateData(Model model, Map<String, Object> data) {
		List<Map<String, Object>> entities = new ArrayList<Map<String, Object>>();
		entities.add(data);
		executeSQL(queryGenerator.generateUpdate(model, entities));
	}
	
	public void deleteData(Model model, Object id) {
		executeSQL(queryGenerator.generateDelete(model, Arrays.asList(new Object[] {id})));
	}

}
