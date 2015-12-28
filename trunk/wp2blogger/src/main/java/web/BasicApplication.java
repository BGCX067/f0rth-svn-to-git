package web;

import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;

import web.page.HomePage;

public class BasicApplication extends WebApplication {
	@Override
	protected void init() {
		super.init();
		getMarkupSettings().setDefaultMarkupEncoding("UTF-8"); // we don't want <?encoding version="1.0" encoding="utf-8"?> in every page
		if (DEPLOYMENT.equalsIgnoreCase(getConfigurationType())) {
			getMarkupSettings().setStripWicketTags(true);
			getMarkupSettings().setCompressWhitespace(true);
			getMarkupSettings().setStripComments(true);
		}
		getApplicationSettings().setPageExpiredErrorPage(HomePage.class);
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
