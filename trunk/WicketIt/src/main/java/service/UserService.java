package service;

import model.User;

import org.springframework.transaction.annotation.Transactional;

import dao.UserDao;

@Transactional
public class UserService {
	private UserDao userDao;

	public void setUserDao(UserDao dao) {
		this.userDao = dao;
	}

	public User authenticate(String email, String password) {
		User user = userDao.findByEmail(email);
		if (null != user && user.authenticate(password)) {
			return user;
		}
		return null;
	}

	public User createUser(User user) throws DuplicatedUserNameException, DuplicatedUserEmailException {
		if (userDao.findByName(user.getName()) != null) {
			throw new DuplicatedUserNameException();
		}
		if (userDao.findByEmail(user.getEmail()) != null) {
			throw new DuplicatedUserEmailException();
		}
		userDao.save(user);
		return user;
	}

	public User findUser(long id) {
		return userDao.load(id);
	}

	public void deleteUser(User user) {
		userDao.delete(user);
	}
}
