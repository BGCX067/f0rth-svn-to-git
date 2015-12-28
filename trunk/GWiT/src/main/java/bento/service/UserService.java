package bento.service;

import bento.model.User;

public interface UserService {
	User authenticate(String email, String password) throws AuthenticationException;
	User createUser(User user) throws UserAlreadyExistException;
	User getUserById(String id) throws UserNotFoundException;
}
