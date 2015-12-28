package web;

import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

import web.page.HomePage;
import web.page.user.SignInPage;
import web.page.user.SignUpPage;
import web.page.user.UserListPage;

public class BasicApplication extends WebApplication {
	@Override
	protected void init() {
		super.init();
		addComponentInstantiationListener(new SpringComponentInjector(this));
		BasicAuthStrategy authStrategy = new BasicAuthStrategy();
		getSecuritySettings().setAuthorizationStrategy(authStrategy);
		getSecuritySettings().setUnauthorizedComponentInstantiationListener(authStrategy);
		getMarkupSettings().setDefaultMarkupEncoding("UTF-8"); // we don't want <?encoding version="1.0" encoding="utf-8"?> on each page
		if (DEPLOYMENT.equalsIgnoreCase(getConfigurationType())) {
			getMarkupSettings().setStripWicketTags(true);
			getMarkupSettings().setCompressWhitespace(true);
			getMarkupSettings().setStripComments(true);
		}
		getApplicationSettings().setPageExpiredErrorPage(HomePage.class);
		mountBookmarkablePage("/signin", SignInPage.class);
		mountBookmarkablePage("/signup", SignUpPage.class);
		mountBookmarkablePage("/users", UserListPage.class);
	}

	@Override
	public Session newSession(Request request, Response response) {
		return new BasicSession(request);
	}

	@Override
	public Class<? extends WebPage> getHomePage() {
		return HomePage.class;
	}
}
