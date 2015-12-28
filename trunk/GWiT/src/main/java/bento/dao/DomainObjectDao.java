package bento.dao;

import bento.model.DomainObject;

public class DomainObjectDao extends AbstractDao<DomainObject> {
	public DomainObject load(Class<DomainObject> clazz, final long id) {
		return (DomainObject)getHibernateTemplate().load(clazz, id);
	}
}
