package web.page.order;

import model.User;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;

import service.DenyOrderException;
import service.OrderService;
import service.UserService;
import web.RequireAdmin;
import web.page.BasePage;
import dto.OrderMenu;

@RequireAdmin
public class AdminOrderPage extends BasePage {
	@SpringBean
	private OrderService orderService;

	@SpringBean
	private UserService userService;

	private Long userId;

	public AdminOrderPage(PageParameters parameters) {
		userId = parameters.getLong("id");
		User user = userService.findUser(userId);
		add(new Label("displayName", user.getDisplayName()));

		final OrderMenu lunchMenu = orderService.getLunchOrderMenu(user);
		if (lunchMenu != null) {
			add(new UserOrderPanel("lunchOrderPanel", lunchMenu) {
				@Override
				public void onSubmit(Form form) {
					try {
						orderService.adminOrder(userService.findUser(userId), lunchMenu);
						form.info("訂餐成功。");
					} catch (DenyOrderException e) {
						form.error("訂餐失敗。");
					}
				}

				@Override
				public void validate(Form form) {
				}
			});
		} else {
			add(new Label("lunchOrderPanel", ""));
		}

		final OrderMenu dinnerMenu = orderService.getDinnerOrderMenu(user);
		if (dinnerMenu != null) {
			add(new UserOrderPanel("dinnerOrderPanel", dinnerMenu) {
				@Override
				public void onSubmit(Form form) {
					try {
						orderService.adminOrder(userService.findUser(userId), dinnerMenu);
						form.info("訂餐成功。");
					} catch (DenyOrderException e) {
						form.error("訂餐失敗。");
					}
				}

				@Override
				public void validate(Form form) {
				}
			});
		} else {
			add(new Label("dinnerOrderPanel", ""));
		}
	}
}
