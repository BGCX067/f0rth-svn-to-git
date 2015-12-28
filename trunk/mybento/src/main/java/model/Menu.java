package model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name="menus")
public class Menu implements DomainObject {
	private Long id;

	private String name;

	private String comment;

	private MealType mealType;

	private Date createdAt;

	private Date updatedAt;

	private List<MenuItem> menuItems;

	private List<Order> orders;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(unique=true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Enumerated(EnumType.STRING)
	public MealType getMealType() {
		return mealType;
	}

	public void setMealType(MealType mealType) {
		this.mealType = mealType;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	@OneToMany(mappedBy="menu", cascade=CascadeType.ALL)
	@OrderBy("providerName, productName")
	public List<MenuItem> getMenuItems() {
		return menuItems;
	}

	public void setMenuItems(List<MenuItem> menuItems) {
		this.menuItems = menuItems;
	}

	@OneToMany(mappedBy="menu")
	@OrderBy("providerName, productName")
	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public void addMenuItems(Provider provider) {
		for (Product product : provider.getProducts()) {
			MenuItem item = new MenuItem();
			item.setMenu(this);
			item.setProvider(provider);
			item.setProviderName(provider.getName());
			item.setProduct(product);
			item.setProductName(product.getName());
			item.setProductPrice(product.getPrice());
			getMenuItems().add(item);
		}
	}
}
