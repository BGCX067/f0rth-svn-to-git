package web.page.provider;

import model.Provider;

import org.apache.wicket.PageParameters;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import service.ProviderService;
import web.RequireAdmin;
import web.model.ProviderDataProvider;
import web.page.BasePage;

@RequireAdmin
public class ListProviderPage extends BasePage {
	@SpringBean
	private ProviderService providerService;

	public ListProviderPage() {
		ProviderDataProvider dataProvider = new ProviderDataProvider();
		DefaultDataTable menus = new DefaultDataTable("providers", createColumns(), dataProvider, 20);
		add(menus);
		add(new BookmarkablePageLink("newProviderLink", NewProviderPage.class));
	}

	public IColumn[] createColumns() {
		int i = 0;
		IColumn[] columns = new IColumn[4];
		columns[i++] = new PropertyColumn(new ResourceModel("name"), "name", "name");
		columns[i++] = new PropertyColumn(new ResourceModel("phone"), "phone", "phone");
		columns[i++] = createEditMenuColumn();
		columns[i++] = createDeleteMenuColumn();
		return columns;
	}

	private IColumn createEditMenuColumn() {
		return new AbstractColumn(new ResourceModel("edit")) {
			public void populateItem(Item cellItem, String componentId, IModel rowModel) {
				cellItem.add(new EditMenuPanel(componentId, rowModel));
			}
		};
	}

	private IColumn createDeleteMenuColumn() {
		return new AbstractColumn(new ResourceModel("delete")) {
			public void populateItem(Item cellItem, String componentId, IModel rowModel) {
				cellItem.add(new DeleteMenuPanel(componentId, rowModel));
			}
		};
	}

	private class EditMenuPanel extends Fragment {
		public EditMenuPanel(String id, IModel model) {
			super(id, "edit", ListProviderPage.this);
			Link editLink = new Link("editLink", model) {
				@Override
				public void onClick() {
					PageParameters params = new PageParameters("id=" + ((Provider)getModelObject()).getId().toString());
					setResponsePage(EditProviderPage.class, params);
				}
			};
			add(editLink);
		}
	}

	private class DeleteMenuPanel extends Fragment {
		public DeleteMenuPanel(String id, IModel model) {
			super(id, "delete", ListProviderPage.this);
			Link deleteLink = new Link("deleteLink", model) {
				@Override
				public void onClick() {
					providerService.deleteProvider((Provider)getModelObject());
				}
			};
			deleteLink.add(new SimpleAttributeModifier("onclick", "return confirm('確定要刪除" + ((Provider)model.getObject()).getName() + "？');"));
			add(deleteLink);
		}
	}
}
