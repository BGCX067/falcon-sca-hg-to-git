package org.sca.calontir.cmpe.db;

import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import org.sca.calontir.cmpe.data.AuthType;

/**
 *
 * @author rik
 */
public class AuthTypeDAO {

    public AuthType  getAuthType(int authTypeId) {
        AuthType authType = null;
        PersistenceManager pm = PMF.get().getPersistenceManager();
        //Key authTypeKey = KeyFactory.createKey(AuthType.class.getSimpleName(), authTypeId);
        authType = (AuthType) pm.getObjectById(AuthType.class, authTypeId);

        return authType;
    }

    public List<AuthType> getAuthType() {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(AuthType.class);
        List<AuthType> atList = (List<AuthType>) query.execute();
        for(AuthType at : atList) {
            System.out.println(at.getAuthTypeId().getId());
        }
        return atList;
    }

    public void saveAuthType(AuthType authType) {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        try {
            authType = pm.makePersistent(authType);
        } finally {
            pm.close();
        }
    }
}
