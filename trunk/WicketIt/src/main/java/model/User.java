package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import util.CryptoUtil;

@Entity
@Table(name="users")
public class User implements DomainObject {
	public static final String SALT = "How I wish I could recollect PI easily using one trick.";

	public static final String ROLE_ADMIN = "admin";

	public static final String ROLE_USER = "user";

	private Long id;

	private String email;

	private String hashedPassword;

	private String name;

	private String fullName;

	private String role;

	public User() {
	}

	public User(String name, String email, String password, String role) {
		this.name = name;
		this.email = email;
		this.hashedPassword = encrypt(password);
		this.role = role;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	private String encrypt(String value) {
		return CryptoUtil.SHA1(SALT + value);
	}

	public boolean authenticate(String password) {
		return getHashedPassword().equals(encrypt(password));
	}

	@Transient
	public boolean isAdmin() {
		return ROLE_ADMIN.equals(getRole());
	}
}
