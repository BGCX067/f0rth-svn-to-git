package web.model;

import java.util.Iterator;

import model.User;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import dao.UserDao;

public class UserDataProvider extends SortableDataProvider {
	@SpringBean
	private UserDao userDao;

	public UserDataProvider() {
		InjectorHolder.getInjector().inject(this);
		setSort("userName", true);
	}

	public Iterator<User> iterator(int first, int count) {
		return userDao.iterator(first, count, getSort().getProperty(), getSort().isAscending());
	}

	public IModel model(Object object) {
		return new DomainObjectModel((User)object);
	}

	public int size() {
		return (int)userDao.countAll();
	}
}
