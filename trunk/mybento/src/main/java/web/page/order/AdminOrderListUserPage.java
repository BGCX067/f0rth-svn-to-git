package web.page.order;

import java.util.List;

import model.User;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.Strings;

import service.UserService;
import web.RequireAdmin;
import web.model.DomainObjectModel;
import web.page.BasePage;

@RequireAdmin
public class AdminOrderListUserPage extends BasePage {
	@SpringBean
	private UserService userService;

	private String filterName;

	public AdminOrderListUserPage() {
		add(new Link("importUsers") {
			@Override
			public void onClick() {
				userService.importUsers();
			}
		});
		add(new FilterForm("filterForm"));
		add(new ListView("items", new UserListModel()) {
			@Override
			protected void populateItem(ListItem item) {
				User user = ((User)item.getModelObject());
				item.add(new Label("userName", user.getUserName()));
				item.add(new Label("displayName", user.getDisplayName()));
				item.add(new BookmarkablePageLink("adminOrderLink", AdminOrderPage.class, new PageParameters("id=" + user.getId())));
			}

			@Override
			protected IModel getListItemModel(IModel listViewModel, int index) {
				return new DomainObjectModel(((List<User>)listViewModel.getObject()).get(index));
			}
		});
	}

	private class UserListModel extends LoadableDetachableModel {
		@Override
		protected Object load() {
			if (Strings.isEmpty(filterName)) {
				return userService.listUser();
			} else {
				return userService.listUser(filterName);
			}
		}
	}

	private class FilterForm extends Form {
		public FilterForm(String id) {
			super(id);
			add(new TextField("filterName", new PropertyModel(AdminOrderListUserPage.this, "filterName")));
		}
	}
}
