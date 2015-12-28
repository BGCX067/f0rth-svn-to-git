package bento.service.impl;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import bento.dao.UserDao;
import bento.model.User;
import bento.service.AuthenticationException;
import bento.service.UserAlreadyExistException;
import bento.service.UserNotFoundException;
import bento.service.UserService;
import bento.util.CryptoUtil;

@Transactional
public class UserServiceImpl implements UserService {
	private UserDao userDao;

	public void setUserDao(UserDao dao) {
		this.userDao = dao;
	}

	@Override
	public User authenticate(String email, String password) throws AuthenticationException {
		User user = new User();
		user.setEmail(email);
		user.setPassword(CryptoUtil.SHA1(password));
		List<User> users = userDao.findByExample(user);
		if (!users.isEmpty()) {
			return users.get(0);
		} else {
			throw new AuthenticationException();
		}
	}

	@Override
	public User createUser(User user) throws UserAlreadyExistException {
		if (userDao.isEmailAvailable(user.getEmail())) {
			user.setPassword(CryptoUtil.SHA1(user.getPassword()));
			user.setRole(User.ROLE_USER);
			userDao.save(user);
			return user;
		} else {
			throw new UserAlreadyExistException();
		}
	}

	@Override
	public User getUserById(String id) throws UserNotFoundException {
		User user = userDao.load(id);
		if (user != null) {
			return user;
		} else {
			throw new UserNotFoundException();
		}
	}
}
