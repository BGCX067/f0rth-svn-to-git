package web;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IUnauthorizedComponentInstantiationListener;
import org.apache.wicket.authorization.strategies.page.AbstractPageAuthorizationStrategy;

import web.page.user.SignInPage;

public class BasicAuthStrategy extends AbstractPageAuthorizationStrategy implements IUnauthorizedComponentInstantiationListener {
	@Override
	public boolean isActionAuthorized(Component component, Action action) {
		if (action.equals(Component.RENDER)) {
			Class<? extends Component> clazz = component.getClass();
			if (clazz.isAnnotationPresent(RequireAdmin.class)) {
				return BasicSession.get().isAdminSignedIn();
			} else if (clazz.isAnnotationPresent(RequireAdmin.class)) {
				return BasicSession.get().isUserSignedIn();
			}
		}
		return true;
	}

	@Override
	protected boolean isPageAuthorized(Class pageClass) {
		if (pageClass.isAnnotationPresent(RequireAdmin.class)) {
			return BasicSession.get().isAdminSignedIn();
		} else if (pageClass.isAnnotationPresent(RequireUser.class)) {
			return BasicSession.get().isUserSignedIn();
		}
		return true;
	}

	public void onUnauthorizedInstantiation(Component component) {
		throw new RestartResponseAtInterceptPageException(SignInPage.class);
	}
}
