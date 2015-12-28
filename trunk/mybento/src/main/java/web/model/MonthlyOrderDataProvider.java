package web.model;

import java.util.Iterator;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import dao.OrderDao;
import dto.YearMonthData;

public class MonthlyOrderDataProvider extends SortableDataProvider {
	@SpringBean
	private OrderDao orderDao;

	public MonthlyOrderDataProvider() {
		InjectorHolder.getInjector().inject(this);
		setSort("yearMonth", false);
	}

	public Iterator<YearMonthData> iterator(int first, int count) {
		return orderDao.monthlyIterator(first, count, getSort().getProperty(), getSort().isAscending());
	}

	public IModel model(final Object object) {
		return new AbstractReadOnlyModel() {
			public Object getObject() {
				return object;
			}
		};
	}

	public int size() {
		return (int)orderDao.countMonthlyOrder();
	}
}
