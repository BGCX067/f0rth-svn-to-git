package dto;

import java.io.Serializable;

import model.MenuItem;

public class MenuItemData implements Serializable {
	private Long menuItemId;

	private String providerName;

	private String productName;

	private int productPrice;

	private boolean selected;

	public Long getMenuItemId() {
		return menuItemId;
	}

	public MenuItemData(MenuItem menuItem) {
		this.menuItemId = menuItem.getId();
		this.providerName = menuItem.getProviderName();
		this.productName = menuItem.getProductName();
		this.productPrice = menuItem.getProductPrice();
	}

	public String getProviderName() {
		return providerName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(int productPrice) {
		this.productPrice = productPrice;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
