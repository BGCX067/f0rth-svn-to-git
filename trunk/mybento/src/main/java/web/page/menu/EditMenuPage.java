package web.page.menu;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import service.MenuService;
import web.RequireAdmin;
import web.page.BasePage;
import dto.MenuData;
import dto.MenuItemData;

@RequireAdmin
public class EditMenuPage extends BasePage {
	@SpringBean
	private MenuService menuService;

	private MenuData editMenu;

	public EditMenuPage(PageParameters parameters) {
		super(parameters);
		editMenu = menuService.getEditMenu(getPageParameters().getLong("id"));
		add(new Label("menuName", editMenu.getName()));
		add(new EditMenuForm("menuForm"));
	}

	private class EditMenuForm extends Form {
		public EditMenuForm(String id) {
			super(id);
			add(new DropDownChoice("newProvider", new PropertyModel(editMenu, "newProvider"), editMenu.getAvailableProviders()));
			add(new TextArea("comment", new PropertyModel(editMenu, "comment")));
			add(new ListView("menuItems", editMenu.getItems()) {
				@Override
				protected void populateItem(ListItem item) {
					MenuItemData a = (MenuItemData)item.getModelObject();
					item.add(new CheckBox("selected", new PropertyModel(a, "selected")));
					item.add(new Label("provider", new PropertyModel(a, "providerName")));
					item.add(new TextField("product", new PropertyModel(a, "productName")));
					item.add(new TextField("price", new PropertyModel(a, "productPrice")));
				}
			});
		}

		@Override
		protected void onSubmit() {
			menuService.saveEditMenu(editMenu);
			setResponsePage(EditMenuPage.class, new PageParameters("id=" + editMenu.getMenuId()));
		}
	}
}
