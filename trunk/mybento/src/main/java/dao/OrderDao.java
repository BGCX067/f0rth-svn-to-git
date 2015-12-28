package dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import model.MealType;
import model.Menu;
import model.Order;
import model.User;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import dto.MonthlyOrderSummary;
import dto.UserMonthlyOrderSummary;
import dto.YearMonthData;

public class OrderDao extends AbstractDao<Order> {
	public List<Order> getOrders(User user, Menu menu) {
		return getHibernateTemplate().find("FROM Order o WHERE o.user=? AND o.menu=?", new Object[] {user, menu});
	}

	public Order findOrder(User user, Long menuItemId) {
		List<Order> list = getHibernateTemplate().find("FROM Order o WHERE o.user=? AND o.menuItem.id=?", new Object[] {user, menuItemId});
		if (list.isEmpty()) {
			return null;
		} else {
			return list.get(0);
		}
	}

	public Iterator<Order> iterator(Long userId, int first, int count, String orderBy, boolean isAscending) {
		DetachedCriteria criteria = DetachedCriteria.forClass(type);
		criteria.add(Restrictions.eq("user.id", userId));
		if (orderBy != null) {
			criteria.addOrder(isAscending ? org.hibernate.criterion.Order.asc(orderBy) : org.hibernate.criterion.Order.desc(orderBy));
		}
		return getHibernateTemplate().findByCriteria(criteria, first, count).iterator();
	}

	public long countAll(Long userId) {
		DetachedCriteria criteria = createCriteria().add(Restrictions.eq("user.id", userId)).setProjection(Projections.rowCount());
		return ((Number)getHibernateTemplate().findByCriteria(criteria).get(0)).longValue();
	}

	public Iterator<YearMonthData> monthlyIterator(int first, int count, String orderBy, boolean isAscending) {
		DetachedCriteria criteria = DetachedCriteria.forClass(type);
		criteria.setProjection(Projections.distinct(Projections.property("yearMonth")));
		if (orderBy != null) {
			criteria.addOrder(isAscending ? org.hibernate.criterion.Order.asc(orderBy) : org.hibernate.criterion.Order.desc(orderBy));
		}
		List<YearMonthData> list = new ArrayList<YearMonthData>();
		for (Number a : (List<Number>)getHibernateTemplate().findByCriteria(criteria, first, count)) {
			list.add(new YearMonthData(a.intValue()));
		}
		return list.iterator();
	}

	public long countMonthlyOrder() {
		DetachedCriteria criteria = createCriteria();
		criteria.setProjection(Projections.countDistinct("yearMonth"));
		return ((Number)getHibernateTemplate().findByCriteria(criteria).get(0)).longValue();
	}

	public MonthlyOrderSummary getOrderSummary(Date begin, Date end) {
		MonthlyOrderSummary summary = new MonthlyOrderSummary();
		List<Object[]> rows = getHibernateTemplate().find("SELECT u.userName, u.displayName, SUM(o.quantity), SUM (o.productPrice * o.quantity) FROM Order o INNER JOIN o.user u WHERE o.mealType=? AND o.createdAt BETWEEN ? AND ? GROUP BY u.userName, u.displayName ORDER BY u.userName", new Object[] {MealType.LUNCH, begin, end});
		for (Object[] a : rows) {
			String userName = (String)a[0];
			String displayName = (String)a[1];
			Number quantity = (Number)a[2];
			Number subtotal = (Number)a[3];
			summary.addItem(userName, displayName, quantity.intValue(), subtotal.intValue());
		}
		return summary;
	}

	public Iterator<YearMonthData> userMonthlyIterator(Long userId, int first, int count, String orderBy, boolean isAscending) {
		DetachedCriteria criteria = DetachedCriteria.forClass(type);
		criteria.add(Restrictions.eq("user.id", userId));
		criteria.setProjection(Projections.distinct(Projections.property("yearMonth")));
		if (orderBy != null) {
			criteria.addOrder(isAscending ? org.hibernate.criterion.Order.asc(orderBy) : org.hibernate.criterion.Order.desc(orderBy));
		}
		List<YearMonthData> list = new ArrayList<YearMonthData>();
		for (Number a : (List<Number>)getHibernateTemplate().findByCriteria(criteria, first, count)) {
			list.add(new YearMonthData(a.intValue()));
		}
		return list.iterator();
	}

	public long countUserMonthlyOrder(Long userId) {
		DetachedCriteria criteria = createCriteria();
		criteria.add(Restrictions.eq("user.id", userId));
		criteria.setProjection(Projections.countDistinct("yearMonth"));
		return ((Number)getHibernateTemplate().findByCriteria(criteria).get(0)).longValue();
	}

	public UserMonthlyOrderSummary getUserOrderSummary(Long userId, Date begin, Date end) {
		UserMonthlyOrderSummary summary = new UserMonthlyOrderSummary();
		List<Order> rows = getHibernateTemplate().find("FROM Order o WHERE o.user.id=? AND o.mealType=? AND o.createdAt BETWEEN ? AND ? ORDER BY o.createdAt", new Object[] {userId, MealType.LUNCH, begin, end});
		for (Order a : rows) {
			summary.addItem(a.getCreatedAt(), a.getProviderName(), a.getProductName(), a.getProductPrice(), a.getQuantity());
		}
		return summary;
	}
}
