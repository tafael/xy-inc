package com.prova.dao;

import java.util.List;

import javax.enterprise.context.Dependent;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.prova.model.Field;
import com.prova.model.Model;

@Dependent
public class FieldDAO extends GenericDAOImpl<Field, Long> implements GenericDAO<Field, Long> {

	@SuppressWarnings("unchecked")
	public List<Field> findByModel(Model model) {
		Criteria cri = createCriteria();
		cri.add(Restrictions.eq("model", model));
		return cri.list();
	}

}
