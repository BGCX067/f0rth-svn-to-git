package dao;

import java.util.Iterator;
import java.util.List;

import model.User;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

@SuppressWarnings("unchecked")
public class UserDao extends AbstractDao<User> {
	public Iterator<User> find(int first, int count, String orderBy, boolean isAscending) {
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
		if (orderBy != null) {
			criteria.addOrder(isAscending ? Order.asc(orderBy) : Order.desc(orderBy));
		}
		return getHibernateTemplate().findByCriteria(criteria, first, count).iterator();
	}

	public User findByEmail(String email) {
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class).add(
			Restrictions.eq("email", email).ignoreCase()
		);
		List<User> users = getHibernateTemplate().findByCriteria(criteria);
		if (users.isEmpty()) {
			return null;
		} else if (users.size() > 1) {
			throw new IllegalStateException("Found more than 1 user for email: " + email);
		}
		return users.get(0);
	}

	public User findByName(String name) {
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class).add(
			Restrictions.eq("name", name).ignoreCase()
		);
		List<User> users = getHibernateTemplate().findByCriteria(criteria);
		if (users.isEmpty()) {
			return null;
		} else if (users.size() > 1) {
			throw new IllegalStateException("Found more than 1 user for name: " + name);
		}
		return users.get(0);
	}
}
