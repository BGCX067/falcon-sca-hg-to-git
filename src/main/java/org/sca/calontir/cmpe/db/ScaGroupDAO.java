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
    public org.sca.calontir.cmpe.dto.ScaGroup getScaGroup(long scaGroupId) {
        ScaGroup scaGroup = null;
        PersistenceManager pm = PMF.get().getPersistenceManager();
        scaGroup = (ScaGroup) pm.getObjectById(ScaGroup.class, scaGroupId);
        org.sca.calontir.cmpe.dto.ScaGroup sg = DataTransfer.convert(scaGroup);
        return sg;
    }
    
    public org.sca.calontir.cmpe.dto.ScaGroup getScaGroupByName(String groupName) {
        ScaGroup scaGroup = null;
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(ScaGroup.class);
        query.setFilter("groupName == scaGroupName");
        query.declareParameters("String scaGroupName");
        List<ScaGroup> sgList = (List<ScaGroup>) query.execute(groupName);
        if(sgList.size() > 0)
           return DataTransfer.convert(sgList.get(0));
        else
            return null;
    }
    
    public List<org.sca.calontir.cmpe.dto.ScaGroup> getScaGroup() {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(ScaGroup.class);
        List<ScaGroup> sgList = (List<ScaGroup>) query.execute();
        List<org.sca.calontir.cmpe.dto.ScaGroup> retList = new ArrayList<org.sca.calontir.cmpe.dto.ScaGroup>();
        for(ScaGroup group : sgList) {
            retList.add(DataTransfer.convert(group));
        }
        return retList;
    }
    
    public void saveScaGroup(org.sca.calontir.cmpe.dto.ScaGroup scaGroup) {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        ScaGroup sg = null;
        Query query = pm.newQuery(ScaGroup.class);
        query.setFilter("groupName == scaGroupName");
        query.declareParameters("String scaGroupName");
        List<ScaGroup> sgList = (List<ScaGroup>) query.execute(scaGroup.getGroupName());
        if(sgList != null && sgList.size() > 0) {
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
        ScaGroup scaGroup = null;
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(ScaGroup.class);
        query.setFilter("groupName == scaGroupName");
        query.declareParameters("String scaGroupName");
        List<ScaGroup> sgList = (List<ScaGroup>) query.execute(groupName);
        if(sgList.size() > 0)
           return sgList.get(0);
        else
            return null;
    }
}
