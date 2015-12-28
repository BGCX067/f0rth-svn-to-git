package dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MonthlyOrderSummary implements Serializable {
	private List<MonthlyOrderSummaryItem> items = new ArrayList<MonthlyOrderSummaryItem>();

	private int totalQuantity;

	private int totalSubtotal;

	public void addItem(String userName, String displayName, int quantity, int subtotal) {
		totalQuantity += quantity;
		totalSubtotal += subtotal;
		items.add(new MonthlyOrderSummaryItem(userName, displayName, quantity, subtotal));
	}

	public List<MonthlyOrderSummaryItem> getItems() {
		return items;
	}

	public int getTotalQuantity() {
		return totalQuantity;
	}

	public int getTotalSubtotal() {
		return totalSubtotal;
	}
}
