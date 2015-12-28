package bento.client.service;

import bento.model.User;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BentoServiceAsync {
	void login(String userName, String password, AsyncCallback<User> callback);
	void logout(AsyncCallback<Void> callback);
	void register(String email, String password, AsyncCallback<Void> callback);
}
