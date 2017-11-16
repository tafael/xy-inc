package com.prova.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.prova.dao.GenericDAO;

public abstract class GenericService<T, ID extends Serializable> {

	protected abstract GenericDAO<T, ID> getGenericDAO();

	public T findById(ID id) {
		return getGenericDAO().findById(id);
	}

	public List<T> findAll() {
		return getGenericDAO().findAll();
	}

	public T saveOrUpdate(T entity) {
		return getGenericDAO().saveOrUpdate(entity);
	}

	public void saveOrUpdateAll(Collection<T> entities) {
		getGenericDAO().saveOrUpdateAll(entities);
	}

	public void delete(T entity) {
		getGenericDAO().delete(entity);
	}

	public void deleteAll() {
		getGenericDAO().deleteAll();
	}

	public void deleteAll(Collection<T> entities) {
		getGenericDAO().deleteAll(entities);
	}

	public ID save(T entity) {
		return getGenericDAO().save(entity);
	}

	public void saveAll(Collection<T> entities) {
		getGenericDAO().saveAll(entities);
	}

	public T merge(T entity) {
		return getGenericDAO().merge(entity);
	}

	public T update(T entity) {
		return getGenericDAO().update(entity);
	}

	public void updateAll(Collection<T> entities) {
		getGenericDAO().updateAll(entities);
	}

	public long countAll() {
		return getGenericDAO().countAll();
	}

	@SuppressWarnings("unchecked")
	public void saveUpdateDelete(List<T> oldList, List<T> newList) {
		for (T t : (List<T>) CollectionUtils.subtract(newList, oldList)) {
			save(t);
		}
		for (T t : (List<T>) CollectionUtils.intersection(newList, oldList)) {
			merge(t);
		}
		for (T t : (List<T>) CollectionUtils.subtract(oldList, newList)) {
			delete(t);
		}
	}

}
