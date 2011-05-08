package org.sca.calontir.cmpe;

import org.sca.calontir.cmpe.common.ScaGroupResource;
import java.util.ArrayList;
import java.util.List;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import org.sca.calontir.cmpe.data.ScaGroup;
import org.sca.calontir.cmpe.db.ScaGroupDAO;

/**
 *
 * @author rik
 */
public class ScaGroupServerResource  extends ServerResource implements ScaGroupResource {
    
    ScaGroupDAO dao = new ScaGroupDAO();

    @Get
    public List<ScaGroup> retrieve() {
        List <ScaGroup> retVal = null;
        
        String skey = (String) getRequestAttributes().get("scaGroupKey");
        if(skey != null) {
            int ikey = Integer.parseInt(skey);
            if (ikey > 0) {
                ScaGroup s = dao.getScaGroup(ikey);
                retVal = new ArrayList<ScaGroup>();
                retVal.add(s);
            }
        }
        if (retVal == null || retVal.isEmpty()) {
            retVal = dao.getScaGroup();
        }
        
        return retVal;
    }

    @Put
    public void store(ScaGroup scaGroup) {
        dao.saveScaGroup(scaGroup);
    }

    @Delete
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
