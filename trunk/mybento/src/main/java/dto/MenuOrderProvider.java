package dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import model.Order;

public class MenuOrderProvider implements Serializable {
	private int quantity;

	private int subtotal;

	private List<MenuOrderSummaryItem> items = new ArrayList<MenuOrderSummaryItem>();

	public void addOrder(Order order) {
		this.quantity += order.getQuantity();
		this.subtotal += order.getSubtotal();
		addItem(order);
	}

	private void addItem(Order order) {
		MenuOrderSummaryItem item = findItem(order.getProviderName(), order.getProductName());
		if (null == item) {
			item = new MenuOrderSummaryItem(order.getProviderName(), order.getProductName(), order.getProductPrice());
			items.add(item);
		}
		item.addQuantity(order.getQuantity());
		if (order.isPaid() != null && order.isPaid().booleanValue()) {
			item.addPaidUser(order);
		} else {
			item.addUnpaidUser(order);
		}
	}

	private MenuOrderSummaryItem findItem(String provider, String product) {
		MenuOrderSummaryItem item = null;
		for (MenuOrderSummaryItem a : items) {
			if (a.getProvider().equals(provider) && a.getProduct().equals(product)) {
				item = a;
				break;
			}
		}
		return item;
	}

	public int getQuantity() {
		return quantity;
	}

	public int getSubtotal() {
		return subtotal;
	}

	public List<MenuOrderSummaryItem> getItems() {
		return items;
	}
}
