package web.page.user;

import model.User;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import service.UserService;
import web.RequireAdmin;
import web.model.AdminDataProvider;
import web.page.BasePage;

@RequireAdmin
public class ListAdminPage extends BasePage {
	@SpringBean
	private UserService userService;

	public ListAdminPage() {
		AdminDataProvider dataProvider = new AdminDataProvider();
		DefaultDataTable users = new DefaultDataTable("admins", createColumns(), dataProvider, 10);
		add(users);
		add(new NewAdminForm("newAdminForm"));
	}

	public IColumn[] createColumns() {
		int i = 0;
		IColumn[] columns = new IColumn[3];
		columns[i++] = new PropertyColumn(new ResourceModel("userName"), "userName", "userName");
		columns[i++] = new PropertyColumn(new ResourceModel("displayName"), "displayName", "displayName");
		columns[i++] = createDeleteMenuColumn();
		return columns;
	}

	private IColumn createDeleteMenuColumn() {
		return new AbstractColumn(new ResourceModel("delete")) {
			public void populateItem(Item cellItem, String componentId, IModel rowModel) {
				cellItem.add(new DeleteAdminPanel(componentId, rowModel));
			}
		};
	}

	private class DeleteAdminPanel extends Fragment {
		public DeleteAdminPanel(String id, IModel model) {
			super(id, "delete", ListAdminPage.this);
			Link deleteLink = new Link("deleteLink", model) {
				@Override
				public void onClick() {
					userService.deleteAdmin(((User)getModelObject()).getUserName());
				}
			};
			deleteLink.add(new SimpleAttributeModifier("onclick", "return confirm('確定要刪除" + ((User)model.getObject()).getUserName() + "？');"));
			add(deleteLink);
		}
	}

	private class NewAdminForm extends Form {
		private String userName;

		public NewAdminForm(String id) {
			super(id);
			add(new TextField("userName", new PropertyModel(this, "userName")));
		}

		@Override
		protected void onSubmit() {
			if (userName != null) {
				userService.createAdmin(userName);
			}
			setResponsePage(ListAdminPage.class);
		}
	}
}
