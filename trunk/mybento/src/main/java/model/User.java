package model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="users")
public class User implements DomainObject {
	public static final String ROLE_ADMIN = "admin";

	public static final String ROLE_USER = "user";

	private Long id;

	private String userName;

	private String displayName;

	private String role;

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
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@OneToMany(mappedBy="user", cascade=CascadeType.ALL)
	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	@Transient
	public boolean isAdmin() {
		return ROLE_ADMIN.equals(getRole());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof User) {
			User a = (User)obj;
			return getUserName().equals(a.getUserName());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getUserName().hashCode();
	}
}
