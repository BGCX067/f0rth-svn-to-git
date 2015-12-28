package dao;

import java.util.List;

import model.DomainObject;

public interface Dao<T extends DomainObject> {
	void delete(T object);
	T load(final long id);
	void save(T object);
	List<T> findAll();
	long countAll();
}
