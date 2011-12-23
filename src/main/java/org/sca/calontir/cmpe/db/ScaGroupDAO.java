package org.sca.calontir.cmpe.db;

import java.util.ArrayList;
import javax.jdo.Query;
import java.util.List;
import javax.jdo.PersistenceManager;
import org.sca.calontir.cmpe.data.ScaGroup;
import org.sca.calontir.cmpe.dto.DataTransfer;

/**
 *
 * @author rik
 */
public class ScaGroupDAO {
    public static class LocalCacheImpl extends LocalCacheAbImpl {
        private static LocalCacheImpl _instance = new LocalCacheImpl();

        public static LocalCacheImpl getInstance() {
            return _instance;
        }
    }
    static private LocalCacheImpl localCache = (LocalCacheImpl) LocalCacheImpl.getInstance();

    public org.sca.calontir.cmpe.dto.ScaGroup getScaGroup(long scaGroupId) {
        org.sca.calontir.cmpe.dto.ScaGroup sg =
                (org.sca.calontir.cmpe.dto.ScaGroup) localCache.getValue(scaGroupId);
        if (sg == null) {
            PersistenceManager pm = PMF.get().getPersistenceManager();
            ScaGroup scaGroup = (ScaGroup) pm.getObjectById(ScaGroup.class, scaGroupId);
            sg = DataTransfer.convert(scaGroup);
            localCache.put(scaGroupId, sg);
        }
        return sg;
    }

    public org.sca.calontir.cmpe.dto.ScaGroup getScaGroupByName(String groupName) {
        org.sca.calontir.cmpe.dto.ScaGroup sg =
                (org.sca.calontir.cmpe.dto.ScaGroup) localCache.getValue(groupName);
        if (sg == null) {
            PersistenceManager pm = PMF.get().getPersistenceManager();
            Query query = pm.newQuery(ScaGroup.class);
            query.setFilter("groupName == scaGroupName");
            query.declareParameters("String scaGroupName");
            List<ScaGroup> sgList = (List<ScaGroup>) query.execute(groupName);
            if (sgList.size() > 0) {
                sg = DataTransfer.convert(sgList.get(0));
                localCache.put(groupName, sg);
            } else {
                sg = null;
            }
        }
        return sg;
    }

    public List<org.sca.calontir.cmpe.dto.ScaGroup> getScaGroup() {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(ScaGroup.class);
        List<ScaGroup> sgList = (List<ScaGroup>) query.execute();
        List<org.sca.calontir.cmpe.dto.ScaGroup> retList = new ArrayList<org.sca.calontir.cmpe.dto.ScaGroup>();
        for (ScaGroup group : sgList) {
            retList.add(DataTransfer.convert(group));
        }
        return retList;
    }

    public void saveScaGroup(org.sca.calontir.cmpe.dto.ScaGroup scaGroup) {
        localCache.clear();
        PersistenceManager pm = PMF.get().getPersistenceManager();

        ScaGroup sg = null;
        Query query = pm.newQuery(ScaGroup.class);
        query.setFilter("groupName == scaGroupName");
        query.declareParameters("String scaGroupName");
        List<ScaGroup> sgList = (List<ScaGroup>) query.execute(scaGroup.getGroupName());
        if (sgList != null && sgList.size() > 0) {
            sg = sgList.get(0);
        }

        sg = DataTransfer.convert(scaGroup, sg);
        try {
            pm.makePersistent(sg);
        } finally {
            pm.close();
        }
    }

    public ScaGroup getScaGroupDOByName(String groupName) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(ScaGroup.class);
        query.setFilter("groupName == scaGroupName");
        query.declareParameters("String scaGroupName");
        List<ScaGroup> sgList = (List<ScaGroup>) query.execute(groupName);
        if (sgList.size() > 0) {
            return sgList.get(0);
        } else {
            return null;
        }
    }
}
