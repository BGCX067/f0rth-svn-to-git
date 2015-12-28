package web.page.order;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import dto.OrderMenu;
import dto.OrderMenuItem;

public abstract class UserOrderPanel extends Panel {
	public UserOrderPanel(String id, OrderMenu orderMenu) {
		super(id);
		add(new Label("menuName", orderMenu.getName()));
		add(new Label("menuComment", orderMenu.getComment()));
		add(new Label("totalQuantity", new PropertyModel(orderMenu, "totalQuantity")));
		add(new Label("totalPrice", new PropertyModel(orderMenu, "totalPrice")));
		add(new OrderForm("orderForm").add(new MenuItemView("menuItems", orderMenu.getItems())));
	}

	public abstract void validate(Form form);

	public abstract void onSubmit(Form form);

	private class OrderForm extends Form {
		public OrderForm(String id) {
			super(id);
			add(new AbstractFormValidator() {
				public FormComponent[] getDependentFormComponents() {
					return null;
				}

				public void validate(Form form) {
					UserOrderPanel.this.validate(form);
				}
			});
		}

		@Override
		protected void onSubmit() {
			UserOrderPanel.this.onSubmit(this);
		}
	}

	private class MenuItemView extends ListView {
		public MenuItemView(String id, List<OrderMenuItem> list) {
			super(id, list);
		}

		@Override
		protected void populateItem(ListItem item) {
			OrderMenuItem m = (OrderMenuItem)item.getModelObject();
			item.add(new Label("provider", m.getProvider()));
			item.add(new Label("product", m.getProduct()));
			item.add(new Label("price", "$" + m.getPrice()));
			item.add(new DropDownChoice("quantity", new PropertyModel(m, "quantity"), Arrays.asList(new Integer[]{0,1,2,3,4,5,6,7,8,9,10})));
		}
	}
}
