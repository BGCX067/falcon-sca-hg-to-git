/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.sca.calontir.cmpe.db;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.sca.calontir.cmpe.dto.DataTransfer;
import org.sca.calontir.cmpe.dto.Treaty;

/**
 *
 * @author rik
 */
public class TreatyDao {

	final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	public static class LocalCacheImpl extends LocalCacheAbImpl {

		private static LocalCacheImpl _instance = new LocalCacheImpl();

		public static LocalCacheImpl getInstance() {
			return _instance;
		}
	}
	static private LocalCacheImpl localCache = (LocalCacheImpl) LocalCacheImpl.getInstance();

	public Treaty getTreaty(long id) {
		Treaty rv = (Treaty) localCache.getValue(id);
		if (rv == null) {
			try {
				Key treatyKey = KeyFactory.createKey("Treaty", id);
				Entity treaty = datastore.get(treatyKey);
				rv = DataTransfer.entityToTreaty(treaty);
				localCache.put(id, rv);
			} catch (EntityNotFoundException ex) {
				Logger.getLogger(TreatyDao.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return rv;
	}

	public Key getTreatyId(long id) {
		try {
			Key treatyKey = KeyFactory.createKey("Treaty", id);
			Entity treaty = datastore.get(treatyKey);
			return treaty.getKey();
		} catch (EntityNotFoundException ex) {
			Logger.getLogger(TreatyDao.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	public Key getTreatyKeyByName(String name) {
		Query.Filter filter = new Query.FilterPredicate("name", Query.FilterOperator.EQUAL, name);
		Query query = new Query("Treaty").setFilter(filter);

		List<Entity> treaties = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
		if(treaties.isEmpty())
			return null;
		Entity entity = treaties.get(0);
		return entity.getKey();
	}

	public List<Treaty> getTreaties() {
		Query q = new Query("Treaty")
				.addSort("name", SortDirection.ASCENDING);

		PreparedQuery pq = datastore.prepare(q);
		List<Entity> entities = pq.asList(FetchOptions.Builder.withDefaults());
		List<Treaty> treaties = new ArrayList<>();
		for(Entity entity : entities) {
			treaties.add(DataTransfer.entityToTreaty(entity));
		}
		return treaties;
	}

	public void saveTreaty(Treaty treaty) {
		localCache.clear();
		Entity entity = DataTransfer.treatyToEntity(treaty);
		datastore.put(entity);
	}
}
