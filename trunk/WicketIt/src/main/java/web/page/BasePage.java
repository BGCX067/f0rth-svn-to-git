package web.page;

import model.User;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;

import web.BasicSession;
import web.page.user.SignInPanel;

public abstract class BasePage extends WebPage {
	protected BasePage() {
		add(homePageLink("homeLink"));
		add(new SignInPanel("signInPanel"));
		add(new Label("pageTitle", new PropertyModel(this, "pageTitle")));
		add(new FeedbackPanel("feedback"));
	}

	public String getPageTitle() {
		String className = getClass().getName();
		int index = className.lastIndexOf('.');
		if (index > 0) {
			return className.substring(index + 1);
		}
		return className;
	}

	public BasicSession getCurrentSession() {
		return (BasicSession)getSession();
	}

	public User getCurrentUser() {
		return getCurrentSession().getCurrentUser();
	}

	public void setCurrentUser(User user) {
		getCurrentSession().setCurrentUser(user);
	}

	public boolean isAdminSignedIn() {
		return getCurrentUser() != null && getCurrentUser().isAdmin();
	}

	public boolean isUserSignedIn() {
		return getCurrentUser() != null;
	}
}
