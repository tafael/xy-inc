package com.prova.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/** DAO generico com implementacoes basicas. */
public abstract class GenericDAOImpl<T, ID extends Serializable> implements GenericDAO<T, ID> {

	@PersistenceContext(unitName = "LocalDSPU")
	public EntityManager entityManager;

	private Class<T> persistentClass;

	@SuppressWarnings("unchecked")
	public GenericDAOImpl() {
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public Session openSession() {
		return entityManager.unwrap(Session.class);
	}

	/**
	 * Cria um novo criteria para a entidade persistente do DAO.
	 */
	public Criteria createCriteria() {
		return openSession().createCriteria(getPersistentClass());
	}

	@SuppressWarnings("unchecked")
	@Override
	public T findById(ID id) {
		Session s = openSession();
		T entity = null;
		Criteria cri = s.createCriteria(getPersistentClass());
		cri.add(Restrictions.eq("id", id));
		entity = (T) cri.uniqueResult();
		return entity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAll() {
		Session s = openSession();
		List<T> result = null;
		result = s.createCriteria(getPersistentClass()).list();
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T saveOrUpdate(T entity) {
		Session s = openSession();
		entity = (T) s.merge(entity);
		s.saveOrUpdate(entity);
		return entity;
	}

	@Override
	public void saveOrUpdateAll(Collection<T> entities) {
		Session s = openSession();
		for (T entity : entities) {
			s.saveOrUpdate(entity);
		}
	}

	@Override
	public T update(T entity) {
		Session s = openSession();
		s.update(entity);
		return entity;
	}

	@Override
	public void updateAll(Collection<T> entities) {
		Session s = openSession();
		for (T entity : entities) {
			s.update(entity);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ID save(T entity) {
		Session s = openSession();
		ID id = (ID) s.save(entity);
		return id;
	}

	@Override
	public void saveAll(Collection<T> entities) {
		Session s = openSession();
		for (T entity : entities) {
			s.save(entity);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T merge(T entity) {
		Session s = openSession();
		entity = (T) s.merge(entity);
		return entity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void delete(T entity) {
		Session s = openSession();
		entity = (T) s.merge(entity);
		s.delete(entity);
		s.flush();
	}

	@Override
	public void deleteAll() {
		Query query = openSession().createQuery("delete from " + getPersistentClass().getName());
		query.executeUpdate();
	}

	@Override
	public void deleteAll(Collection<T> entities) {
		Session s = openSession();
		for (T entity : entities) {
			s.delete(entity);
		}
	}

	@Override
	public long countAll() {
		return (Long) openSession().createCriteria(getPersistentClass()).setProjection(Projections.rowCount()).uniqueResult();
	}

	@Override
	public void detach(T entity) {
		entityManager.detach(entity);
	}

	public Class<T> getPersistentClass() {
		return persistentClass;
	}

}
