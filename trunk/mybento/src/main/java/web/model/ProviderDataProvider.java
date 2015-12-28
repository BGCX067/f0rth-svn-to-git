package web.model;

import java.util.Iterator;

import model.Provider;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import dao.ProviderDao;

public class ProviderDataProvider extends SortableDataProvider {
	@SpringBean
	private ProviderDao providerDao;

	public ProviderDataProvider() {
		InjectorHolder.getInjector().inject(this);
		setSort("name", false);
	}

	public Iterator<Provider> iterator(int first, int count) {
		return providerDao.iterator(first, count, getSort().getProperty(), getSort().isAscending());
	}

	public IModel model(Object object) {
		return new DomainObjectModel((Provider)object);
	}

	public int size() {
		return (int)providerDao.countAll();
	}
}
