package com.prova.service;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.transaction.Transactional;

import com.prova.dao.FieldDAO;
import com.prova.dao.GenericDAO;
import com.prova.model.Field;
import com.prova.model.Model;

@Transactional
@Dependent
public class FieldService extends GenericService<Field, Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FieldDAO fieldDAO;
	
	public List<Field> findByModel(Model model) {
		return fieldDAO.findByModel(model);
	}

	@Override
	protected GenericDAO<Field, Long> getGenericDAO() {
		return fieldDAO;
	}

}
