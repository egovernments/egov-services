package org.egov.egf.budget.domain.repository;

public interface CommonDbRepository<T> {

	T save(T entity);

	T update(T entity);

	T findById(T entity);

}
