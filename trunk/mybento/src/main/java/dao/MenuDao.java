package dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import model.Menu;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unchecked")
public class MenuDao extends AbstractDao<Menu> {
	private static final Logger LOG = LoggerFactory.getLogger(MenuDao.class);

	private Menu findByName(String name) {
		DetachedCriteria criteria = createCriteria().add(Restrictions.eq("name", name));

		List<Menu> menus = getHibernateTemplate().findByCriteria(criteria);
		if (menus.size() == 1) {
			return menus.get(0);
		} else if (menus.size() > 1) {
			LOG.error("Found more than 1 lunch menu: " + name);
		}
		return null;
	}

	public Menu findLunchMenu() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String menuName = sdf.format(new Date()) + " 午餐";
		return findByName(menuName);
	}

	public Menu findDinnerMenu() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String menuName = sdf.format(new Date()) + " 加班晚餐";
		return findByName(menuName);
	}

	private long countByName(String name) {
		DetachedCriteria criteria = createCriteria().add(Restrictions.eq("name", name)).setProjection(Projections.rowCount());
		return ((Number)getHibernateTemplate().findByCriteria(criteria).get(0)).longValue();
	}

	public boolean hasLunchMenu() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String menuName = sdf.format(new Date()) + " 午餐";
		return countByName(menuName) == 1;
	}

	public boolean hasDinnerMenu() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String menuName = sdf.format(new Date()) + " 晚餐";
		return countByName(menuName) == 1;
	}
}
