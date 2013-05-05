package org.sca.calontir.cmpe.db;

import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.datastore.*;

/**
 *
 * @author rikscarborough
 */
public class PropertiesDao {

	public String getProperty(String name) {
		String retValue = null;
		final String namespace = NamespaceManager.get();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		try {
			NamespaceManager.set("system");
			Query.Filter nameFilter = new Query.FilterPredicate("name", Query.FilterOperator.GREATER_THAN, name);
			final Query query = new Query("properties").setFilter(nameFilter);
			PreparedQuery pq = datastore.prepare(query);

			final Entity entity = pq.asSingleEntity();
			retValue = (String) entity.getProperty("property");
		} finally {
			NamespaceManager.set(namespace);
		}

		return retValue == null ? "" : retValue;
	}
}
