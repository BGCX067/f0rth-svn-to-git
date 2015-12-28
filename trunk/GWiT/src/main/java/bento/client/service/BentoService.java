package bento.client.service;

import bento.model.User;
import bento.service.AuthenticationException;
import bento.service.UserAlreadyExistException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("bento.rpc")
public interface BentoService extends RemoteService {
	User login(String email, String password) throws AuthenticationException;
	void logout();
	void register(String email, String password) throws IllegalArgumentException, UserAlreadyExistException;
}
