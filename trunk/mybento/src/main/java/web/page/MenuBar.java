package web.page;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;

import web.BasicSession;
import web.page.menu.ListMenuPage;
import web.page.menu.NewMenuPage;
import web.page.order.AdminOrderListUserPage;
import web.page.order.ListMonthlyOrderPage;
import web.page.order.ListUserMonthlyOrderPage;
import web.page.provider.ListProviderPage;
import web.page.user.ListAdminPage;

public class MenuBar extends Panel {
	public MenuBar(String id) {
		super(id);
		add(new BookmarkablePageLink("userOrderLink", getApplication().getHomePage()));
		add(new BookmarkablePageLink("userMonthlyOrdersLink", ListUserMonthlyOrderPage.class));

		add(new BookmarkablePageLink("adminOrderLink", AdminOrderListUserPage.class));
		add(new BookmarkablePageLink("newMenuLink", NewMenuPage.class));
		add(new BookmarkablePageLink("menusLink", ListMenuPage.class));
		add(new BookmarkablePageLink("monthlyOrdersLink", ListMonthlyOrderPage.class));
		add(new BookmarkablePageLink("providerLink", ListProviderPage.class));
		add(new BookmarkablePageLink("adminsLink", ListAdminPage.class) {
			@Override
			public boolean isVisible() {
				return BasicSession.get().isAdminSignedIn();
			}
		});
	}
}
