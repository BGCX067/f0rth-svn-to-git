package web.page.order;

import java.text.SimpleDateFormat;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;

import service.OrderService;
import web.RequireUser;
import web.page.BasePage;
import dto.UserMonthlyOrderSummary;
import dto.UserMonthlyOrderSummaryItem;

@RequireUser
public class ViewUserMonthlyOrderPage extends BasePage {
	@SpringBean
	private OrderService orderService;

	private UserMonthlyOrderSummary summary;

	public ViewUserMonthlyOrderPage(PageParameters parameters) {
		super(parameters);
		int yearMonth = getPageParameters().getInt("id");
		int year = yearMonth / 100;
		int month = yearMonth % 100;
		summary = orderService.getUserMonthlyOrderSummary(getCurrentUser().getId(), yearMonth);
		add(new Label("year", Integer.toString(year)));
		add(new Label("month", Integer.toString(month)));
		add(new Label("totalQuantity", Integer.toString(summary.getTotalQuantity())));
		add(new Label("totalSubtotal", "$" + summary.getTotalSubtotal()));
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		add(new ListView("items", summary.getItems()) {
			@Override
			protected void populateItem(ListItem item) {
				UserMonthlyOrderSummaryItem a = (UserMonthlyOrderSummaryItem)item.getModelObject();
				item.add(new Label("date", sdf.format(a.getDate())));
				item.add(new Label("provider", a.getProvider()));
				item.add(new Label("product", a.getProduct()));
				item.add(new Label("price", "$" + a.getPrice()));
				item.add(new Label("quantity", Integer.toString(a.getQuantity())));
				item.add(new Label("subtotal", "$" + a.getSubtotal()));
			}
		});
	}
}
