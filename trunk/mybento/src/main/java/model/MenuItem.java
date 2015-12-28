package model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name="menu_items")
public class MenuItem implements DomainObject {
	private Long id;

	private String providerName;

	private String productName;

	private Integer productPrice;

	private Menu menu;

	private Provider provider;

	private Product product;

	private List<Order> orders;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(Integer productPrice) {
		this.productPrice = productPrice;
	}

	@ManyToOne
	@JoinColumn(name="menu_id")
	@ForeignKey(name="fk_menu")
	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	@ManyToOne(optional=true)
	@JoinColumn(name="provider_id")
	@ForeignKey(name="fk_provider")
	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	@ManyToOne(optional=true)
	@JoinColumn(name="product_id")
	@ForeignKey(name="fk_product")
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@OneToMany(mappedBy="menuItem")
	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof MenuItem) {
			MenuItem a = (MenuItem)obj;
			return getProviderName().equals(a.getProviderName()) &&
				   getProductName().equals(a.getProductName()) &&
				   getProductPrice().equals(a.getProductPrice());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (getProviderName() + getProductName() + getProductPrice()).hashCode();
	}
}
