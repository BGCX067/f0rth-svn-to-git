package dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class UserMonthlyOrderSummary implements Serializable {
	private List<UserMonthlyOrderSummaryItem> items = new ArrayList<UserMonthlyOrderSummaryItem>();

	private int totalQuantity;

	private int totalSubtotal;

	public void addItem(Date date, String provider, String product, int price, int quantity) {
		UserMonthlyOrderSummaryItem a = new UserMonthlyOrderSummaryItem(date, provider, product, price, quantity);
		totalQuantity += a.getQuantity();
		totalSubtotal += a.getSubtotal();
		items.add(a);
	}

	public List<UserMonthlyOrderSummaryItem> getItems() {
		return Collections.unmodifiableList(items);
	}

	public int getTotalQuantity() {
		return totalQuantity;
	}

	public int getTotalSubtotal() {
		return totalSubtotal;
	}
}
