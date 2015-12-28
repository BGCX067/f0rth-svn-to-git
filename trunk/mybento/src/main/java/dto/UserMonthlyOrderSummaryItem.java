package dto;

import java.io.Serializable;
import java.util.Date;

public class UserMonthlyOrderSummaryItem implements Serializable {
	private Date date;

	private String provider;

	private String product;

	private int price;

	private int quantity;

	public UserMonthlyOrderSummaryItem(Date date, String provider, String product, int price, int quantity) {
		this.date = date;
		this.provider = provider;
		this.product = product;
		this.price = price;
		this.quantity = quantity;
	}

	public Date getDate() {
		return date;
	}

	public String getProvider() {
		return provider;
	}

	public String getProduct() {
		return product;
	}

	public int getPrice() {
		return price;
	}

	public int getQuantity() {
		return quantity;
	}

	public int getSubtotal() {
		return price * quantity;
	}
}
