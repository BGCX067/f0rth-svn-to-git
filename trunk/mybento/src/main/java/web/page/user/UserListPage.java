package web.page.user;

import model.User;

import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import service.UserService;
import web.RequireAdmin;
import web.model.UserDataProvider;
import web.page.BasePage;

@RequireAdmin
public class UserListPage extends BasePage {
	private Long userIdToDelete;

	public UserListPage() {
		UserDataProvider dataProvider = new UserDataProvider();
		DefaultDataTable users = new DefaultDataTable("users", createColumns(), dataProvider, 25);
		add(users);
	}

	public IColumn[] createColumns() {
		int i = 0;
		IColumn[] columns = new IColumn[4];
		columns[i++] = new PropertyColumn(new ResourceModel("userName"), "userName", "userName");
		columns[i++] = new PropertyColumn(new ResourceModel("displayName"), "displayName", "displayName");
		columns[i++] = new PropertyColumn(new ResourceModel("role"), "role", "role");
		columns[i++] = new AbstractColumn(new ResourceModel("action")) {
			public void populateItem(Item cellItem, String componentId, IModel rowModel) {
				if (((User)rowModel.getObject()).getId().equals(userIdToDelete)) {
					cellItem.add(new ConfirmDeleteUserPanel(componentId, rowModel));
				} else {
					cellItem.add(new UserActionPanel(componentId, rowModel));
				}
			}
		};
		return columns;
	}

	private class UserActionPanel extends Fragment {
		public UserActionPanel(String id, IModel model) {
			super(id, "delete", UserListPage.this);
			Link deleteLink = new Link("deleteLink", model) {
				@Override
				public void onClick() {
					userIdToDelete = ((User)getModelObject()).getId();
				}
			};
			add(deleteLink);
		}
	}

	private class ConfirmDeleteUserPanel extends Fragment {
		@SpringBean
		private UserService userService;

		public ConfirmDeleteUserPanel(String id, IModel model) {
			super(id, "confirm", UserListPage.this);
			add(new Link("confirmLink", model) {
				@Override
				public void onClick() {
					userService.deleteUser((User)getModel().getObject());
				}
			});
			add(new Link("cancelLink") {
				@Override
				public void onClick() {
					userIdToDelete = null;
				}
			});
		}
	}
}
