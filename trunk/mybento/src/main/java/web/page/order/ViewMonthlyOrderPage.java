package web.page.order;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;

import service.OrderService;
import web.RequireAdmin;
import web.page.BasePage;
import dto.MonthlyOrderSummary;
import dto.MonthlyOrderSummaryItem;

@RequireAdmin
public class ViewMonthlyOrderPage extends BasePage {
	@SpringBean
	private OrderService orderService;

	private MonthlyOrderSummary summary;

	public ViewMonthlyOrderPage(PageParameters parameters) {
		super(parameters);
		int yearMonth = getPageParameters().getInt("id");
		int year = yearMonth / 100;
		int month = yearMonth % 100;
		summary = orderService.getMonthlyOrderSummary(yearMonth);
		add(new Label("year", Integer.toString(year)));
		add(new Label("month", Integer.toString(month)));
		add(new Label("totalQuantity", Integer.toString(summary.getTotalQuantity())));
		add(new Label("totalSubtotal", "$" + summary.getTotalSubtotal()));
		add(new ListView("items", summary.getItems()) {
			@Override
			protected void populateItem(ListItem item) {
				MonthlyOrderSummaryItem a = (MonthlyOrderSummaryItem)item.getModelObject();
				item.add(new Label("userName", a.getUserName()));
				item.add(new Label("displayName", a.getDisplayName()));
				item.add(new Label("quantity", Integer.toString(a.getQuantity())));
				item.add(new Label("subtotal", "$" + a.getSubtotal()));
			}
		});
	}
}
