package web.page;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;

import service.DenyOrderException;
import service.OrderService;
import web.BasicSession;
import web.RequireUser;
import web.page.order.UserOrderPanel;
import dto.OrderMenu;

@RequireUser
public class HomePage extends BasePage {
	@SpringBean
	private OrderService orderService;

	public HomePage() {
		final OrderMenu lunchMenu = orderService.getLunchOrderMenu(getCurrentUser());
		if (lunchMenu != null) {
			add(new UserOrderPanel("lunchOrderPanel", lunchMenu) {
				@Override
				public void onSubmit(Form form) {
					try {
						orderService.order(BasicSession.get().getCurrentUser(), lunchMenu);
						form.info("訂餐成功。");
					} catch (DenyOrderException e) {
						form.error("訂餐失敗。");
					}
				}

				@Override
				public void validate(Form form) {
					if (lunchMenu.isOutdated()) {
						form.error("已經超過訂餐時間。");
					}
				}
			});
		} else {
			add(new Label("lunchOrderPanel", ""));
		}

		final OrderMenu dinnerMenu = orderService.getDinnerOrderMenu(getCurrentUser());
		if (dinnerMenu != null) {
			add(new UserOrderPanel("dinnerOrderPanel", dinnerMenu) {
				@Override
				public void onSubmit(Form form) {
					try {
						orderService.order(BasicSession.get().getCurrentUser(), dinnerMenu);
						form.info("訂餐成功。");
					} catch (DenyOrderException e) {
						form.error("訂餐失敗。");
					}
				}

				@Override
				public void validate(Form form) {
					if (dinnerMenu.isOutdated()) {
						form.error("已經超過訂餐時間。");
					}
				}
			});
		} else {
			add(new Label("dinnerOrderPanel", ""));
		}
	}
}
