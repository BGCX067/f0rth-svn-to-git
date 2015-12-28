package bento.dao;

import java.io.Serializable;
import java.util.List;

import bento.model.DomainObject;

public interface Dao<T extends DomainObject> {
	void delete(T object);
	T load(Serializable id);
	void save(T object);
	List<T> findAll();
	long countAll();
}
