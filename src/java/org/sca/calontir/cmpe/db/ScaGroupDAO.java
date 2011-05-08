package org.sca.calontir.cmpe.db;

import javax.jdo.Query;
import java.util.List;
import javax.jdo.PersistenceManager;
import org.sca.calontir.cmpe.data.ScaGroup;

/**
 *
 * @author rik
 */
public class ScaGroupDAO {
    public ScaGroup getScaGroup(int scaGroupId) {
        ScaGroup scaGroup = null;
        PersistenceManager pm = PMF.get().getPersistenceManager();
        scaGroup = (ScaGroup) pm.getObjectById(ScaGroup.class, scaGroupId);
        return scaGroup;
    }
    
    public List<ScaGroup> getScaGroup() {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(ScaGroup.class);
        List<ScaGroup> retList = (List<ScaGroup>) query.execute();
        return retList;
    }
    
    public void saveScaGroup(ScaGroup scaGroup) {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        try {
            scaGroup = pm.makePersistent(scaGroup);
        } finally {
            pm.close();
        }
    }
}
