package dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;

import model.DomainObject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public abstract class AbstractDao<T extends DomainObject> extends HibernateDaoSupport implements Dao<T> {
	protected final Class<T> type;

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

	public T load(long id) {
		return (T)getHibernateTemplate().get(type, id);
	}

	public void save(T object) {
		getHibernateTemplate().saveOrUpdate(object);
	}

	public List<T> findAll() {
		return getHibernateTemplate().findByCriteria(createCriteria());
	}

	public List<T> findAll(String filterName) {
		return getHibernateTemplate().findByCriteria(
			createCriteria().add(
				Restrictions.or(
					Restrictions.ilike("userName", filterName, MatchMode.ANYWHERE),
					Restrictions.ilike("displayName", filterName, MatchMode.ANYWHERE)
				)
			)
		);
	}

	public List<T> findByExample(T object) {
		return getHibernateTemplate().findByCriteria(createCriteria().add(Example.create(object)));
	}

	public long countAll() {
		DetachedCriteria criteria = createCriteria().setProjection(Projections.rowCount());
		return ((Number)getHibernateTemplate().findByCriteria(criteria).get(0)).longValue();
	}

	public Iterator<T> iterator(int first, int count, String orderBy, boolean isAscending) {
		DetachedCriteria criteria = createCriteria();
		if (orderBy != null) {
			criteria.addOrder(isAscending ? Order.asc(orderBy) : Order.desc(orderBy));
		}
		return getHibernateTemplate().findByCriteria(criteria, first, count).iterator();
	}
}
