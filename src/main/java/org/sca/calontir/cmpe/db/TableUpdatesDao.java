package org.sca.calontir.cmpe.db;

import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import org.sca.calontir.cmpe.data.TableUpdates;

/**
 *
 * @author rikscarborough
 */
public class TableUpdatesDao {

    public static class LocalCacheImpl extends LocalCacheAbImpl {

        private static LocalCacheImpl _instance = new LocalCacheImpl();

        public static LocalCacheImpl getInstance() {
            return _instance;
        }
    }
    static private LocalCacheImpl localCache = (LocalCacheImpl) LocalCacheImpl.getInstance();
    private final PersistenceManager pm = PMF.get().getPersistenceManager();

    private void loadTableToCache() {
        Query query = pm.newQuery(TableUpdates.class);
        List<TableUpdates> tableUpdates = (List<TableUpdates>) query.execute();
        for (TableUpdates tu : tableUpdates) {
            org.sca.calontir.cmpe.dto.TableUpdates dtoTu = new org.sca.calontir.cmpe.dto.TableUpdates();
            dtoTu.setTableUpdatesId(tu.getTableUpdatesId().getId());
            dtoTu.setTableName(tu.getTableName());
            dtoTu.setLastUpdated(tu.getLastUpdated());
            dtoTu.setLastDeletion(tu.getLastDeletion());
            localCache.put(dtoTu.getTableName(), dtoTu);
        }
    }

    public org.sca.calontir.cmpe.dto.TableUpdates getTableUpdates(String table) {
        org.sca.calontir.cmpe.dto.TableUpdates rv = (org.sca.calontir.cmpe.dto.TableUpdates) localCache.getValue(table);
        if (rv == null) {
            loadTableToCache();
            rv = (org.sca.calontir.cmpe.dto.TableUpdates) localCache.getValue(table);
        }

        return rv;
    }

    public List<org.sca.calontir.cmpe.dto.TableUpdates> getTableUpdates() {
        List<org.sca.calontir.cmpe.dto.TableUpdates> tableUpdates = localCache.getValueList();
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
    protected void saveTableUpdates(TableUpdates tableUpdates) {
        localCache.clear();

        pm.makePersistent(tableUpdates);
    }
}
