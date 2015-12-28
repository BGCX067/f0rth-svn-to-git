package service;

import java.util.Iterator;

import model.Menu;
import model.MenuItem;
import model.Order;
import model.User;

import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Transactional;

import dao.MenuDao;
import dao.MenuItemDao;
import dao.OrderDao;
import dto.MenuOrderSummary;
import dto.MonthlyOrderSummary;
import dto.OrderMenu;
import dto.OrderMenuItem;
import dto.UserMonthlyOrderSummary;

@Transactional
public class OrderService {
	private MenuDao menuDao;

	private MenuItemDao menuItemDao;

	private OrderDao orderDao;

	public void setMenuDao(MenuDao menuDao) {
		this.menuDao = menuDao;
	}

	public void setMenuItemDao(MenuItemDao menuItemDao) {
		this.menuItemDao = menuItemDao;
	}

	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	@Transactional(readOnly=true)
	public OrderMenu getLunchOrderMenu(User user) {
		Menu menu = menuDao.findLunchMenu();
		if (menu != null) {
			return new OrderMenu(menu, orderDao.getOrders(user, menu));
		}
		return null;
	}

	@Transactional(readOnly=true)
	public OrderMenu getDinnerOrderMenu(User user) {
		Menu menu = menuDao.findDinnerMenu();
		if (menu != null) {
			return new OrderMenu(menu, orderDao.getOrders(user, menu));
		}
		return null;
	}

	private Order createOrUpdateOrder(User user, Long menuItemId, int quantity) {
		DateTime now = new DateTime();
		MenuItem menuItem = menuItemDao.load(menuItemId);

		Order o = orderDao.findOrder(user, menuItemId);
		if (null == o) {
			o = new Order();
			o.setCreatedAt(now.toDate());
			// orders after 25th of the month belongs to next month
			if (now.dayOfMonth().get() > 25) {
				DateTime next = now.plusMonths(1);
				o.setYearMonth(next.getYear() * 100 + next.getMonthOfYear());
			} else {
				o.setYearMonth(now.getYear() * 100 + now.getMonthOfYear());
			}
			o.setPaid(false);
		}

		o.setUser(user);
		o.setMenu(menuItem.getMenu());
		o.setMealType(menuItem.getMenu().getMealType());
		o.setMenuItem(menuItem);
		o.setQuantity(quantity);
		o.setUpdatedAt(now.toDate());

		orderDao.save(o);

		return o;
	}

	private void deleteOrder(User user, Long menuItemId) {
		Iterator<Order> iter = menuItemDao.load(menuItemId).getOrders().iterator();
		while (iter.hasNext()) {
			Order o = iter.next();
			if (user.equals(o.getUser())) {
				iter.remove();
				orderDao.delete(o);
			}
		}
	}

	public void order(User user, OrderMenu orderMenu) throws DenyOrderException {
		if (orderMenu.isOutdated()) {
			throw new DenyOrderException();
		}
		for (OrderMenuItem a : orderMenu.getItems()) {
			if (a.getQuantity() > 0) {
				createOrUpdateOrder(user, a.getMenuItemId(), a.getQuantity());
			} else if (a.getQuantity() <= 0) {
				deleteOrder(user, a.getMenuItemId());
			}
		}
	}

	public void adminOrder(User user, OrderMenu orderMenu) throws DenyOrderException {
		for (OrderMenuItem a : orderMenu.getItems()) {
			if (a.getQuantity() > 0) {
				createOrUpdateOrder(user, a.getMenuItemId(), a.getQuantity());
			} else if (a.getQuantity() <= 0) {
				deleteOrder(user, a.getMenuItemId());
			}
		}
	}

	public MenuOrderSummary getMenuOrderSummary(Long menuId) {
		return new MenuOrderSummary(menuDao.load(menuId));
	}

	public void markOrderPaid(Long orderId) {
		Order order = orderDao.load(orderId);
		order.setPaid(Boolean.TRUE);
	}

	public void markOrderUnpaid(Long orderId) {
		Order order = orderDao.load(orderId);
		order.setPaid(Boolean.FALSE);
	}

	public MonthlyOrderSummary getMonthlyOrderSummary(int yearMonth) {
		int year = yearMonth / 100;
		int month = yearMonth % 100;
		DateTime begin = new DateTime().withDate(year, month, 26).withTime(0, 0, 0, 0).minusMonths(1);
		DateTime end   = new DateTime().withDate(year, month, 25).withTime(23, 59, 59, 0);
		return orderDao.getOrderSummary(begin.toDate(), end.toDate());
	}

	public UserMonthlyOrderSummary getUserMonthlyOrderSummary(Long userId, int yearMonth) {
		int year = yearMonth / 100;
		int month = yearMonth % 100;
		DateTime begin = new DateTime().withDate(year, month, 26).withTime(0, 0, 0, 0).minusMonths(1);
		DateTime end   = new DateTime().withDate(year, month, 25).withTime(23, 59, 59, 0);
		return orderDao.getUserOrderSummary(userId, begin.toDate(), end.toDate());
	}
}
