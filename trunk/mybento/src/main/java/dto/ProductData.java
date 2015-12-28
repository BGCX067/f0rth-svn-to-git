package dto;

import java.io.Serializable;

import model.Product;

public class ProductData implements Serializable {
	private Long productId;

	private String name;

	private int price;

	private boolean selected;

	public ProductData(Product product) {
		this.productId = product.getId();
		this.name = product.getName();
		this.price = product.getPrice();
	}

	public ProductData(String name, int price) {
		this.name = name;
		this.price = price;
	}

	public Long getProductId() {
		return productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
