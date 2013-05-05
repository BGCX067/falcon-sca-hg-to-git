package org.sca.calontir.cmpe.db;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import java.util.Date;
import java.util.List;
import org.sca.calontir.cmpe.dto.TableUpdates;

/**
 *
 * @author rikscarborough
 */
public class TableUpdatesDao {

	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	public static class LocalCacheImpl extends LocalCacheAbImpl {

		private static LocalCacheImpl _instance = new LocalCacheImpl();

		public static LocalCacheImpl getInstance() {
			return _instance;
		}
	}
	static private LocalCacheImpl localCache = (LocalCacheImpl) LocalCacheImpl.getInstance();

	private void loadTableToCache() {
		Query query = new Query("TableUpdates");
		List<Entity> tableUpdates = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
		for (Entity tu : tableUpdates) {
			TableUpdates dtoTu = new TableUpdates();
			dtoTu.setTableUpdatesId(tu.getKey().getId());
			dtoTu.setTableName((String) tu.getProperty("tableName"));
			dtoTu.setLastUpdated((Date) tu.getProperty("lastUpdated"));
			dtoTu.setLastDeletion((Date) tu.getProperty("lastDeletion"));
			localCache.put(dtoTu.getTableName(), dtoTu);
		}
	}

	public org.sca.calontir.cmpe.dto.TableUpdates getTableUpdates(String table) {
		TableUpdates rv = (TableUpdates) localCache.getValue(table);
		if (rv == null) {
			loadTableToCache();
			rv = (TableUpdates) localCache.getValue(table);
		}

		return rv;
	}

	public List<TableUpdates> getTableUpdates() {
		List<TableUpdates> tableUpdates = localCache.getValueList();
		if (tableUpdates == null || tableUpdates.isEmpty()) {
			loadTableToCache();
			tableUpdates = localCache.getValueList();
		}
		return tableUpdates;
	}

	/*
	 * Should only be called from other dao's, so don't flush or close
	 * the pm.
	 */
	protected Key saveTableUpdates(TableUpdates tableUpdates) {
		localCache.clear();

		Key key = KeyFactory.createKey("TableUpdates", tableUpdates.getTableUpdatesId());
		Entity entity;
		if (tableUpdates.getTableUpdatesId() == null || tableUpdates.getTableUpdatesId() == 0) {
			entity = new Entity("TableUpdates");
		} else {
			entity = new Entity(key);
		}
		entity.setProperty("lastDeletion", tableUpdates.getLastDeletion());
		entity.setProperty("lastUpdated", tableUpdates.getLastUpdated());
		entity.setProperty("tableName", tableUpdates.getTableName());
		return datastore.put(entity);
	}
}
