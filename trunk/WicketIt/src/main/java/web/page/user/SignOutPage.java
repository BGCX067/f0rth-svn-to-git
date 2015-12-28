package web.page.user;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.WebPage;

public class SignOutPage extends WebPage {
	public SignOutPage() {
		getSession().invalidate();
		throw new RestartResponseAtInterceptPageException(getApplication().getHomePage());
	}
}
