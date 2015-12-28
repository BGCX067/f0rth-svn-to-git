package web.page.menu;

import model.Menu;

import org.apache.wicket.PageParameters;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import service.MenuService;
import web.RequireAdmin;
import web.model.MenuDataProvider;
import web.page.BasePage;

@RequireAdmin
public class ListMenuPage extends BasePage {
	@SpringBean
	private MenuService menuService;

	public ListMenuPage() {
		MenuDataProvider dataProvider = new MenuDataProvider();
		DefaultDataTable menus = new DefaultDataTable("menus", createColumns(), dataProvider, 20);
		add(menus);
	}

	public IColumn[] createColumns() {
		int i = 0;
		IColumn[] columns = new IColumn[4];
		columns[i++] = new PropertyColumn(new ResourceModel("name"), "name", "name");
		columns[i++] = createEditMenuColumn();
		columns[i++] = createDeleteMenuColumn();
		columns[i++] = createViewMenuColumn();
		return columns;
	}

	private IColumn createViewMenuColumn() {
		return new AbstractColumn(new ResourceModel("view")) {
			public void populateItem(Item cellItem, String componentId, IModel rowModel) {
				cellItem.add(new ViewMenuPanel(componentId, rowModel));
			}
		};
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

	private class ViewMenuPanel extends Panel {
		public ViewMenuPanel(String id, IModel model) {
			super(id);
			Link editLink = new Link("viewLink", model) {
				@Override
				public void onClick() {
					PageParameters params = new PageParameters("id=" + ((Menu)getModelObject()).getId().toString());
					setResponsePage(ViewMenuPage.class, params);
				}
			};
			add(editLink);
		}
	}

	private class EditMenuPanel extends Panel {
		public EditMenuPanel(String id, IModel model) {
			super(id);
			Link editLink = new Link("editLink", model) {
				@Override
				public void onClick() {
					PageParameters params = new PageParameters("id=" + ((Menu)getModelObject()).getId().toString());
					setResponsePage(EditMenuPage.class, params);
				}
			};
			add(editLink);
		}
	}

	private class DeleteMenuPanel extends Panel {
		public DeleteMenuPanel(String id, IModel model) {
			super(id);
			Link deleteLink = new Link("deleteLink", model) {
				@Override
				public void onClick() {
					menuService.deleteMenu((Menu)getModelObject());
				}
			};
			deleteLink.add(new SimpleAttributeModifier("onclick", "return confirm('確定要刪除" + ((Menu)model.getObject()).getName() + "？');"));
			add(deleteLink);
		}
	}
}
