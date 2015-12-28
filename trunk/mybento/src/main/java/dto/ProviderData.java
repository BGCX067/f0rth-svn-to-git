package dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import model.Product;
import model.Provider;

public class ProviderData implements Serializable {
	private Long providerId;

	private String name;

	private String phone;

	private String address;

	private List<ProductData> products;

	private ProductData newProduct;

	public ProviderData(Provider provider) {
		this.providerId = provider.getId();
		this.name = provider.getName();
		this.phone = provider.getPhone();
		this.address = provider.getAddress();
		initProducts(provider);
	}

	private void initProducts(Provider provider) {
		products = new ArrayList<ProductData>();
		for (Product a : provider.getProducts()) {
			products.add(new ProductData(a));
		}
	}

	public Long getProviderId() {
		return providerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<ProductData> getProducts() {
		return products;
	}

	public ProductData getNewProduct() {
		return newProduct;
	}

	public void setNewProduct(ProductData newProduct) {
		this.newProduct = newProduct;
	}
}
