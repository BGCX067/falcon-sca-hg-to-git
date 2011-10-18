/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.db;

import com.google.appengine.api.datastore.Key;
import javax.jdo.PersistenceManager;
import org.sca.calontir.cmpe.data.Treaty;
import org.sca.calontir.cmpe.dto.DataTransfer;

/**
 *
 * @author rik
 */
public class TreatyDao {
    public org.sca.calontir.cmpe.dto.Treaty getTreaty(long id) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Treaty treaty = (Treaty) pm.getObjectById(Treaty.class, id);
        return DataTransfer.convert(treaty);
    }
    
    public Key getTreatyId(long id) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Treaty treaty = (Treaty) pm.getObjectById(Treaty.class, id);
        return treaty.getTreatyId();
    }
}
