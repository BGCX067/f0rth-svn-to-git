package dao;

import java.util.List;

import model.Provider;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProviderDao extends AbstractDao<Provider> {
	private static final Logger LOG = LoggerFactory.getLogger(ProviderDao.class); 

	public Provider findByName(String name) {
		DetachedCriteria criteria = createCriteria().add(Restrictions.eq("name", name));

		List<Provider> menus = getHibernateTemplate().findByCriteria(criteria);
		if (menus.size() == 1) {
			return menus.get(0);
		} else if (menus.size() > 1) {
			LOG.error("Found more than 1 provider: " + name);
		}
		return null;
	}

	public List<Provider> findAll() {
		return getHibernateTemplate().findByCriteria(createCriteria().addOrder(Order.asc("name")));
	}
}
