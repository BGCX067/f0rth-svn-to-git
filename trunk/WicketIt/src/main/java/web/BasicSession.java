package web;

import model.User;

import org.apache.wicket.Request;
import org.apache.wicket.Session;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.protocol.http.WebSession;

import web.model.DomainObjectModel;

public class BasicSession extends WebSession {
	private DomainObjectModel userModel;

	public static BasicSession get() {
		return (BasicSession)Session.get();
	}

	public BasicSession(Request request) {
		super(request);
		InjectorHolder.getInjector().inject(this);
	}

	@Override
	protected void detach() {
		super.detach();
		if (userModel != null) userModel.detach();
	}

	public User getCurrentUser() {
		if (userModel != null) {
			return (User)userModel.getObject();
		}
		return null;
	}

	public void setCurrentUser(User user) {
		userModel = new DomainObjectModel(user);
	}

	public boolean isAdminSignedIn() {
		return isUserSignedIn() && getCurrentUser().isAdmin();
	}

	public boolean isUserSignedIn() {
		return getCurrentUser() != null;
	}
}
