package bento.server;

import javax.servlet.http.HttpSession;

import org.gwtwidgets.server.spring.ServletUtils;

import bento.client.service.BentoService;
import bento.model.User;
import bento.service.AuthenticationException;
import bento.service.UserAlreadyExistException;
import bento.service.UserService;

public class BentoServiceImpl implements BentoService {
	private UserService userService;

	public void setUserService(UserService service) {
		this.userService = service;
	}

	private HttpSession getSession() {
		return ServletUtils.getRequest().getSession();
	}

	private String getCurrentUserId() {
		return (String)getSession().getAttribute("current_user_id");
	}

	private void setCurrentUserId(String userId) {
		getSession().setAttribute("current_user_id", userId);
	}

	@Override
	public User login(String email, String password) throws AuthenticationException {
		User user = userService.authenticate(email, password);
		setCurrentUserId(user.getId());
		return user.copy();
	}

	@Override
	public void logout() {
		getSession().invalidate();
	}

	@Override
	public void register(String email, String password) throws IllegalArgumentException, UserAlreadyExistException {
		if (null == email || (email = email.trim()).isEmpty()) {
			throw new IllegalArgumentException("Email is empty.");
		}

		if (null == password || password.isEmpty()) {
			throw new IllegalArgumentException("Password is empty.");
		}

		User user = new User();
		user.setEmail(email);
		user.setPassword(password);

		userService.createUser(user);
	}
}
