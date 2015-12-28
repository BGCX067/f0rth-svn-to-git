package web.model;

import java.util.Iterator;

import model.Menu;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import dao.MenuDao;

public class MenuDataProvider extends SortableDataProvider {
	@SpringBean
	private MenuDao menuDao;

	public MenuDataProvider() {
		InjectorHolder.getInjector().inject(this);
		setSort("name", false);
	}

	public Iterator<Menu> iterator(int first, int count) {
		return menuDao.iterator(first, count, getSort().getProperty(), getSort().isAscending());
	}

	public IModel model(Object object) {
		return new DomainObjectModel((Menu)object);
	}

	public int size() {
		return (int)menuDao.countAll();
	}
}
