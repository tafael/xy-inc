package com.prova.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.collections.CollectionUtils;

import com.prova.model.Field;
import com.prova.model.Model;
import com.prova.model.TypeEnum;
import com.prova.query.api.QueryGenerator;

/**
 * FIXME Esta classe apresenta vulnerabilidade a SQL injection, uma vez que
 * monta uma query crua apenas concatenando os parametros vindos da requisição.
 * É necessário uma implementação usando PreparedStatement para evitar esse
 * problema.
 */
public class PostgreSQLQueryGenerator implements QueryGenerator {
	
	private String getColumnStatement(Field column) {
		StringBuilder sb = new StringBuilder();
		sb.append(column.getName() + " ");
		sb.append(column.getType().sqlStatement + " ");
		if (column.getType() == TypeEnum.CHARACTER && column.getSize() != null) {
			sb.append("(" + column.getSize() + ") ");
		}
		if (column.getNotNull()) {
			sb.append("not null");
		}
		return sb.toString();
	}

	@Override
	public List<String> generateCreateTable(Model table) {
		List<String> statements = new ArrayList<String>();
		statements.add("create sequence seq_" + table.getName());
		
		StringBuilder sql = new StringBuilder();
		sql.append("create table " + table.getName() + " (");
		sql.append("id integer not null default nextval('seq_" + table.getName() + "')");
		for (Field column : table.getFields()) {
			sql.append("," + getColumnStatement(column));
		}
		sql.append(",constraint pk_" + table.getName() + " primary key (id)");
		// TODO foreign keys
		sql.append(")");
		statements.add(sql.toString());
		
		return statements;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> generateAlterTable(Model _old, Model _new) {
		List<String> statements = new ArrayList<String>();
		
		for (Field column : (List<Field>) CollectionUtils.subtract(_new.getFields(), _old.getFields())) {
			statements.add("alter table " + _old.getName() + " add column " + getColumnStatement(column));
		}
		/*for (Field column : (List<Field>) CollectionUtils.intersection(_new.getFields(), _old.getFields())) {
			// TODO
		}*/
		for (Field column : (List<Field>) CollectionUtils.subtract(_old.getFields(), _new.getFields())) {
			statements.add("alter table " + _old.getName() + " drop column " + column.getName());
		}
		
		if (!_old.getName().equals(_new.getName())) {
			statements.add("alter table " + _old.getName() + " rename to " + _new.getName());
			statements.add("alter sequence seq_" + _old.getName() + " rename to " + _new.getName());
		}
		return statements;
	}

	/** Retorna o valor formatado para ser usado em um script SQL Postgres. */
	private static String getSQLFormattedValue(Object o) {
		if (o == null) {
			return null;
		}
		if (o instanceof String) {
			return '\'' + (String) o + '\'';
		}
		if (o instanceof java.util.Date) {
			return '\'' + o.toString() + '\'';
		}
		if (o.getClass().isEnum()) {
			Enum<?> e = (Enum<?>) o;
			return '\'' + e.name() + '\'';
		}
		return o.toString();
	}

	private static String extractColumnValue(Field column, Map<String, Object> entity) {
		return getSQLFormattedValue(entity.get(column.getName()));
	}

	@Override
	public String generateSelect(Model table, List<Map<String, Object>> params) {
		StringBuilder sql = new StringBuilder();
		sql.append("select ");
		
		String sep = "";
		for (Field column : table.getFields()) {
			sql.append(sep + column.getName());
			sep = ",";
		}
		
		sql.append(" from " + table.getName());
		if (params != null && !params.isEmpty()) {
			sql.append(" where ");
			Stack<String> seps  = new Stack<String>();
			seps.push("");
			seps.push("");
			for (Map<String, Object> param : params) {
				sql.append(seps.pop());
				for (String column : param.keySet()) {
					sql.append(seps.pop() + column);
					sql.append("=");
					sql.append(getSQLFormattedValue(param.get(column)));
					seps.push(" and ");
				}
				seps.push(" or ");
			}
		}
		// TODO order by
		return sql.toString();
	}

	@Override
	public String generateInsert(Model table, List<Map<String, Object>> entities) {
		StringBuilder sql = new StringBuilder();

		String sep = "";
		sql.append("insert into " + table.getName() + "(");
		for (Field column : table.getFields()) {
			if (table.getTableId().equals(column)) continue;
			sql.append(sep + column.getName());
			sep = ",";
		}
		// sql.append(sep + table.getTableId().getName());
		sep = "";
		sql.append(") values (");

		for (Map<String, Object> entity : entities) {
			for (Field column : table.getFields()) {
				if (table.getTableId().equals(column)) continue;
				sql.append(sep + extractColumnValue(column, entity));
				sep = ",";
			}
			// sql.append(sep + extractColumnValue(table.getTableId(), entity));
			sql.append(") returning id ");
		}
		return sql.toString();
	}

	@Override
	public String generateUpdate(Model table, List<Map<String, Object>> entities) {
		StringBuilder sql = new StringBuilder();
		boolean flag = true;
		String sep = "";
		for (Map<String, Object> entity : entities) {
			sql.append("update " + table.getName() + " set ");
			for (Field column : table.getFields()) {
				if (table.getTableId().equals(column)) continue;
				sql.append(sep + column.getName());
				sql.append("=");
				sql.append(extractColumnValue(column, entity));
				sep = ",";
				flag = false;
			}
			sql.append(" where " + table.getTableId().getName() + " = " + extractColumnValue(table.getTableId(), entity) + ";");
		}
		if (flag)
			return "";

		return sql.toString();
	}

	@Override
	public List<String> generateDelete(Model table, List<Object> ids) {
		List<String> deleteStatements = new ArrayList<String>();
		for (Object id : ids) {
			deleteStatements.add("delete from " + table.getName() + " where " + table.getTableId().getName() + "=" + getSQLFormattedValue(id));
		}
		return deleteStatements;
	}

}
