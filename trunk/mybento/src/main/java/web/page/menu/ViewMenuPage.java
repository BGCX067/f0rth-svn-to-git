package web.page.menu;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;

import service.OrderService;
import web.RequireAdmin;
import web.page.BasePage;
import dto.MenuOrderProvider;
import dto.MenuOrderSummary;
import dto.MenuOrderSummaryItem;
import dto.MenuOrderSummaryUser;

@RequireAdmin
public class ViewMenuPage extends BasePage {
	@SpringBean
	private OrderService orderService;

	private Long menuId;

	private MenuOrderSummary summary;

	public ViewMenuPage(PageParameters parameters) {
		super(parameters);
		menuId = getPageParameters().getLong("id");
		summary = orderService.getMenuOrderSummary(menuId);
		add(new Label("menuName", summary.getMenuName()));
		add(new Label("totalPrice", "$" + summary.getSubtotal()));
		add(new Label("totalQuantity", Integer.toString(summary.getQuantity())));
		add(new ListView("providers", summary.getSummaries()) {
			@Override
			protected void populateItem(ListItem item) {
				MenuOrderProvider a = (MenuOrderProvider)item.getModelObject();
				item.add(new Label("sumOfPrice", "$" + a.getSubtotal()));
				item.add(new Label("sumOfQuantity", Integer.toString(a.getQuantity())));
				item.add(new ListView("products", a.getItems()) {
					@Override
					protected void populateItem(ListItem item) {
						MenuOrderSummaryItem a = (MenuOrderSummaryItem)item.getModelObject();
						item.add(new Label("provider", a.getProvider()));
						item.add(new Label("product", a.getProduct()));
						item.add(new Label("price", "$" + a.getPrice()));
						item.add(new Label("quantity", Integer.toString(a.getQuantity())));
						item.add(new ListView("paidUsers", a.getPaidUsers()) {
							@Override
							protected void populateItem(ListItem item) {
								final MenuOrderSummaryUser a = (MenuOrderSummaryUser)item.getModelObject();
								Link link = new Link("markUnpaidLink") {
									@Override
									public void onClick() {
										orderService.markOrderUnpaid(a.getOrderId());
										setResponsePage(ViewMenuPage.class, new PageParameters("id=" + menuId));
									}
								};
								link.add(new Label("paidUser", String.format("%s(%d)", a.getDisplayName(), a.getQuantity())));
								item.add(link);
							}
						});
						item.add(new ListView("unpaidUsers", a.getUnpaidUsers()) {
							@Override
							protected void populateItem(ListItem item) {
								final MenuOrderSummaryUser a = (MenuOrderSummaryUser)item.getModelObject();
								Link link = new Link("markPaidLink") {
									@Override
									public void onClick() {
										orderService.markOrderPaid(a.getOrderId());
										setResponsePage(ViewMenuPage.class, new PageParameters("id=" + menuId));
									}
								};
								link.add(new Label("unpaidUser", String.format("%s(%d)", a.getDisplayName(), a.getQuantity())));
								item.add(link);
							}
						});
					}
				});
			}
		});
	}
}
