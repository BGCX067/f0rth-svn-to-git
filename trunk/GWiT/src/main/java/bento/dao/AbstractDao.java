package bento.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import bento.model.DomainObject;

public abstract class AbstractDao<T extends DomainObject> extends HibernateDaoSupport implements Dao<T> {
	private final Class<T> type;

	public DetachedCriteria createCriteria() {
		return DetachedCriteria.forClass(type);
	}

	public AbstractDao() {
		Class clazz = getClass();
		Type type = clazz.getGenericSuperclass();
		while (!(type instanceof ParameterizedType)) {
			clazz = clazz.getSuperclass();
			type = clazz.getGenericSuperclass();
		}
		this.type = (Class<T>)((ParameterizedType)type).getActualTypeArguments()[0];
	}

	public void delete(T object) {
		getHibernateTemplate().delete(object);
	}

	public T load(Serializable id) {
		return (T)getHibernateTemplate().get(type, id);
	}

	public void save(T object) {
		getHibernateTemplate().saveOrUpdate(object);
	}

	public List<T> findAll() {
		return getHibernateTemplate().findByCriteria(createCriteria());
	}

	public List<T> findByExample(T object) {
		return getHibernateTemplate().findByCriteria(createCriteria().add(Example.create(object)));
	}

	public long countAll() {
		DetachedCriteria criteria = createCriteria().setProjection(Projections.rowCount());
		return ((Number)getHibernateTemplate().findByCriteria(criteria).get(0)).longValue();
	}
}
