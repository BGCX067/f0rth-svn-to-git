package dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import model.Menu;
import model.MenuItem;
import model.Order;

import org.joda.time.DateTime;

public class OrderMenu implements Serializable {
	private Long menuId;

	private String name;

	private String comment;

	private List<OrderMenuItem> items;

	public OrderMenu(Menu menu, List<Order> orders) {
		this.menuId = menu.getId();
		this.name = menu.getName();
		this.comment = menu.getComment();
		initItems(menu, orders);
	}

	private void initItems(Menu menu, List<Order> orders) {
		items = new ArrayList<OrderMenuItem>();
		for (MenuItem a : menu.getMenuItems()) {
			items.add(createOrderMenuItem(a, orders));
		}
	}

	private OrderMenuItem createOrderMenuItem(MenuItem menuItem, List<Order> orders) {
		int quantity = 0;
		for (Order a : orders) {
			if (menuItem.equals(a.getMenuItem())) {
				quantity = a.getQuantity();
				break;
			}
		}
		return new OrderMenuItem(menuItem.getId(), menuItem.getProviderName(), menuItem.getProductName(), menuItem.getProductPrice(), quantity);
	}

	public Long getMenuId() {
		return menuId;
	}

	public String getName() {
		return name;
	}

	public String getComment() {
		return comment;
	}

	public List<OrderMenuItem> getItems() {
		return items;
	}

	public int getTotalQuantity() {
		int sum = 0;
		for (OrderMenuItem a : items) {
			sum += a.getQuantity();
		}
		return sum;
	}

	public int getTotalPrice() {
		int sum = 0;
		for (OrderMenuItem a : items) {
			sum += a.getQuantity() * a.getPrice();
		}
		return sum;
	}

	public boolean isOutdated() {
		if (name.indexOf("午餐") > 0 && new DateTime().withTime(10, 5, 0, 0).isBeforeNow()) {
			return true;
		} else if (name.indexOf("晚餐") > 0 && new DateTime().withTime(17, 5, 0, 0).isBeforeNow()) {
			return true;
		}
		return false;
	}
}
