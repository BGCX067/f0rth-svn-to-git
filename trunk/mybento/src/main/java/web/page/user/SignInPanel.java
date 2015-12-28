package web.page.user;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import web.BasicSession;

public class SignInPanel extends Panel {
	public SignInPanel(String id) {
		super(id);
		add(new BookmarkablePageLink("signInLink", SignInPage.class) {
			@Override
			public boolean isVisible() {
				return !BasicSession.get().isUserSignedIn();
			}
		});
		add(new Label("displayName", new PropertyModel(this, "session.currentUser.displayName")));
		add(new BookmarkablePageLink("signOutLink", SignOutPage.class) {
			@Override
			public boolean isVisible() {
				return BasicSession.get().isUserSignedIn();
			}
		});
	}
}
