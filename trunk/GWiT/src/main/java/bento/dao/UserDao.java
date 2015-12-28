package bento.dao;

import java.util.Iterator;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import bento.model.User;
import bento.service.UserNotFoundException;

@SuppressWarnings("unchecked")
public class UserDao extends AbstractDao<User> {
	public Iterator<User> find(int first, int count, String orderBy, boolean isAscending) {
		DetachedCriteria criteria = createCriteria();
		if (orderBy != null) {
			criteria.addOrder(isAscending ? Order.asc(orderBy) : Order.desc(orderBy));
		}
		return getHibernateTemplate().findByCriteria(criteria, first, count).iterator();
	}

	public boolean isEmailAvailable(String email) {
		DetachedCriteria criteria = createCriteria().add(
			Restrictions.eq("email", email).ignoreCase()
		).setProjection(Projections.rowCount());
		return 0 == ((Number)getHibernateTemplate().findByCriteria(criteria).get(0)).longValue();
	}

	public User findByEmail(String email) throws UserNotFoundException {
		DetachedCriteria criteria = createCriteria().add(
			Restrictions.eq("email", email).ignoreCase()
		);
		List<User> users = getHibernateTemplate().findByCriteria(criteria);
		if (users.isEmpty()) {
			throw new UserNotFoundException();
		} else if (users.size() > 1) {
			throw new IllegalStateException("Found more than 1 user for email: " + email);
		}
		return users.get(0);
	}
}
