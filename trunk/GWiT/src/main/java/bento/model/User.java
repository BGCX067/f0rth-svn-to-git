package bento.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name="users")
public class User implements DomainObject {
	public static final String ROLE_ADMIN = "admin";

	public static final String ROLE_USER = "user";

	private String id;

	private String email;

	private String password;

	private String role;

	@Id
	@GeneratedValue(generator="uuid") 
	@GenericGenerator(name="uuid", strategy="uuid", parameters={@Parameter(name="separator", value="-")})
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public User copy() {
		User user = new User();
		user.id = id;
		user.email = email;
		user.password = password;
		user.role = role;
		return user;
	}

	public String toString() {
		return "{id: " + id + ", email: " + email + ", password: " + password + ", role: " + role + "}";
	}
}
