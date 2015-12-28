package web.model;

import model.DomainObject;

import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import dao.DomainObjectDao;

public class DomainObjectModel extends LoadableDetachableModel {
	@SpringBean
	private DomainObjectDao domainObjectDao;

	private final Class<DomainObject> type;

	private final Long id;

	public DomainObjectModel(DomainObject domainObject) {
		super(domainObject);
		InjectorHolder.getInjector().inject(this);
		this.type = (Class<DomainObject>)domainObject.getClass();
		this.id = domainObject.getId(); 
	}

	public DomainObjectModel(Class<DomainObject> clazz, final Long id) {
		InjectorHolder.getInjector().inject(this);
		this.type = clazz;
		this.id = id;
	}

	@Override
	protected Object load() {
		return domainObjectDao.load(type, id);
	}
}
