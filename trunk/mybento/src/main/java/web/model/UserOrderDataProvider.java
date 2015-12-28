package web.model;

import java.util.Iterator;

import model.Order;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import dao.OrderDao;

public class UserOrderDataProvider extends SortableDataProvider {
	@SpringBean
	private OrderDao orderDao;

	private Long userId;

	public UserOrderDataProvider(Long userId) {
		InjectorHolder.getInjector().inject(this);
		setSort("createdAt", false);
		this.userId = userId;
	}

	public Iterator<Order> iterator(int first, int count) {
		return orderDao.iterator(userId, first, count, getSort().getProperty(), getSort().isAscending());
	}

	public IModel model(Object object) {
		return new DomainObjectModel((Order)object);
	}

	public int size() {
		return (int)orderDao.countAll(userId);
	}
}
