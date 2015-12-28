package web.page.order;

import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import web.RequireAdmin;
import web.model.MonthlyOrderDataProvider;
import web.page.BasePage;
import dto.YearMonthData;

@RequireAdmin
public class ListMonthlyOrderPage extends BasePage {
	public ListMonthlyOrderPage() {
		MonthlyOrderDataProvider dataProvider = new MonthlyOrderDataProvider();
		DefaultDataTable menus = new DefaultDataTable("items", createColumns(), dataProvider, 20);
		add(menus);
	}

	public IColumn[] createColumns() {
		int i = 0;
		IColumn[] columns = new IColumn[2];
		columns[i++] = new PropertyColumn(new ResourceModel("yearMonth"), "yearMonth", "yearMonth") {
			@Override
			public void populateItem(Item item, String componentId, IModel model) {
				YearMonthData a = (YearMonthData)model.getObject();
				item.add(new Label(componentId, String.format("%d年%d月", a.getYear(), a.getMonth())));
			}
		};
		columns[i++] = createViewColumn();
		return columns;
	}

	private IColumn createViewColumn() {
		return new AbstractColumn(new ResourceModel("view")) {
			public void populateItem(Item cellItem, String componentId, IModel rowModel) {
				cellItem.add(new ViewOrderPanel(componentId, rowModel));
			}
		};
	}

	private class ViewOrderPanel extends Fragment {
		public ViewOrderPanel(String id, IModel model) {
			super(id, "view", ListMonthlyOrderPage.this);
			Link editLink = new Link("viewLink", model) {
				@Override
				public void onClick() {
					PageParameters params = new PageParameters("id=" + ((YearMonthData)getModelObject()).getYearMonth());
					setResponsePage(ViewMonthlyOrderPage.class, params);
				}
			};
			add(editLink);
		}
	}
}
