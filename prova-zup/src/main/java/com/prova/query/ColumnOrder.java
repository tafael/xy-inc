package com.prova.query;

import com.prova.model.Field;
import com.prova.query.api.Order;

public class ColumnOrder implements Order {

	private Field column;

	private boolean ascending = true;

	public ColumnOrder(Field column) {
		this.column = column;
	}

	public ColumnOrder(Field column, boolean ascending) {
		this.column = column;
		this.ascending = ascending;
	}

	@Override
	public String getSQL() {
		return column.getName() + (!ascending ? " desc" : "");
	}

}
