package dao;

import java.util.Iterator;
import java.util.List;

import model.User;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class UserDao extends AbstractDao<User> {
	public User findByUserName(String userName) {
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class).add(
			Restrictions.eq("userName", userName).ignoreCase()
		);
		List<User> users = getHibernateTemplate().findByCriteria(criteria);
		if (users.isEmpty()) {
			return null;
		} else if (users.size() > 1) {
			throw new IllegalStateException("Found more than 1 user for name: " + userName);
		}
		return users.get(0);
	}

	public Iterator<User> adminIterator(int first, int count, String orderBy, boolean isAscending) {
		DetachedCriteria criteria = DetachedCriteria.forClass(type).add(Restrictions.eq("role", User.ROLE_ADMIN));
		if (orderBy != null) {
			criteria.addOrder(isAscending ? Order.asc(orderBy) : Order.desc(orderBy));
		}
		return getHibernateTemplate().findByCriteria(criteria, first, count).iterator();
	}

	public long countAdmin() {
		DetachedCriteria criteria = createCriteria().add(Restrictions.eq("role", User.ROLE_ADMIN)).setProjection(Projections.rowCount());
		return ((Number)getHibernateTemplate().findByCriteria(criteria).get(0)).longValue();
	}
}
