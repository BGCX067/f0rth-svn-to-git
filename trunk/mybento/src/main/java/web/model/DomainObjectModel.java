package web.model;

import model.DomainObject;

import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import dao.DomainObjectDao;

public class DomainObjectModel extends LoadableDetachableModel {
	@SpringBean
	private DomainObjectDao domainObjectDao;

	private final Class<? extends DomainObject> type;

	private final Long id;

	public DomainObjectModel(DomainObject domainObject) {
		super(domainObject);
		InjectorHolder.getInjector().inject(this);
		this.type = findNonProxyType(domainObject);
		this.id = domainObject.getId();
	}

	public DomainObjectModel(Class<DomainObject> clazz, final Long id) {
		InjectorHolder.getInjector().inject(this);
		this.type = clazz;
		this.id = id;
	}

	private Class<? extends DomainObject> findNonProxyType(DomainObject domainObject) {
		Class<? extends DomainObject> type = domainObject.getClass();
		if (type.getName().indexOf("$EnhancerByCGLIB$") > 0) {
			type = (Class<? extends DomainObject>)type.getSuperclass();
		}
		return type;
	}

	@Override
	protected Object load() {
		return domainObjectDao.load(type, id);
	}
}
