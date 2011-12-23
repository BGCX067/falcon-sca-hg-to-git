/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.sca.calontir.cmpe.db;

import com.google.appengine.api.datastore.Key;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import org.sca.calontir.cmpe.data.Treaty;
import org.sca.calontir.cmpe.dto.DataTransfer;

/**
 *
 * @author rik
 */
public class TreatyDao {
    public static class LocalCacheImpl extends LocalCacheAbImpl {
        private static LocalCacheImpl _instance = new LocalCacheImpl();

        public static LocalCacheImpl getInstance() {
            return _instance;
        }
    }
    static private LocalCacheImpl localCache = (LocalCacheImpl) LocalCacheImpl.getInstance();
    private final PersistenceManager pm = PMF.get().getPersistenceManager();

    public org.sca.calontir.cmpe.dto.Treaty getTreaty(long id) {
        org.sca.calontir.cmpe.dto.Treaty rv = 
                (org.sca.calontir.cmpe.dto.Treaty) localCache.getValue(id);
        if (rv == null) {
            Treaty treaty = (Treaty) pm.getObjectById(Treaty.class, id);
            rv = DataTransfer.convert(treaty);
            localCache.put(id, rv);
        }
        return rv;
    }

    public Key getTreatyId(long id) {
        Treaty treaty = (Treaty) pm.getObjectById(Treaty.class, id);
        return treaty.getTreatyId();
    }

    public List<Treaty> getTreaties() {
        Query query = pm.newQuery(Treaty.class);
        List<Treaty> treaty = (List<Treaty>) query.execute();
        return treaty;
    }

    public void saveTreaty(Treaty treaty) {
        localCache.clear();
        try {
            treaty = pm.makePersistent(treaty);
            pm.flush();
        } finally {
            pm.close();
        }
    }
}
