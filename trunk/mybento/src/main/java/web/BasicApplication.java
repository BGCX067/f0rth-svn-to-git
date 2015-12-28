package web;

import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

import web.page.HomePage;
import web.page.menu.EditMenuPage;
import web.page.menu.ListMenuPage;
import web.page.menu.NewMenuPage;
import web.page.menu.ViewMenuPage;
import web.page.order.AdminOrderListUserPage;
import web.page.order.AdminOrderPage;
import web.page.order.ListMonthlyOrderPage;
import web.page.order.ListUserMonthlyOrderPage;
import web.page.order.ViewMonthlyOrderPage;
import web.page.order.ViewUserMonthlyOrderPage;
import web.page.provider.EditProviderPage;
import web.page.provider.ListProviderPage;
import web.page.provider.NewProviderPage;
import web.page.user.ListAdminPage;
import web.page.user.SignInPage;
import web.page.user.SignOutPage;
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
		mountBookmarkablePage("/signout", SignOutPage.class);
		mountBookmarkablePage("/users", UserListPage.class);
		mountBookmarkablePage("/admins", ListAdminPage.class);
		mountBookmarkablePage("/my/orders", ListUserMonthlyOrderPage.class);
		mountBookmarkablePage("/my/order", ViewUserMonthlyOrderPage.class);
		mountBookmarkablePage("/new_menu", NewMenuPage.class);
		mountBookmarkablePage("/edit_menu", EditMenuPage.class);
		mountBookmarkablePage("/view_menu", ViewMenuPage.class);
		mountBookmarkablePage("/menus", ListMenuPage.class);
		mountBookmarkablePage("/providers", ListProviderPage.class);
		mountBookmarkablePage("/new_provider", NewProviderPage.class);
		mountBookmarkablePage("/edit_provider", EditProviderPage.class);
		mountBookmarkablePage("/monthly_orders", ListMonthlyOrderPage.class);
		mountBookmarkablePage("/view_monthly_order", ViewMonthlyOrderPage.class);
		mountBookmarkablePage("/admin_order_users", AdminOrderListUserPage.class);
		mountBookmarkablePage("/admin_order", AdminOrderPage.class);
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
