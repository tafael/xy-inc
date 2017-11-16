package com.prova.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface GenericDAO<T, ID extends Serializable> {
	
	T findById(ID id);

	List<T> findAll();

	T saveOrUpdate(T entity);

	void saveOrUpdateAll(Collection<T> entities);

	void delete(T entity);

	void deleteAll();

	void deleteAll(Collection<T> entities);

	ID save(T entity);

	void saveAll(Collection<T> entities);

	T merge(T entity);

	T update(T entity);

	void updateAll(Collection<T> entities);

	long countAll();

	void detach(T entity);

}
