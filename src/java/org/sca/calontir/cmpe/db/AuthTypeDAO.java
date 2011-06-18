package org.sca.calontir.cmpe.db;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import java.util.ArrayList;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import org.sca.calontir.cmpe.data.AuthType;
import org.sca.calontir.cmpe.dto.DataTransfer;

/**
 *
 * @author rik
 */
public class AuthTypeDAO {

    public org.sca.calontir.cmpe.dto.AuthType  getAuthType(long authTypeId) {
        AuthType authType = null;
        PersistenceManager pm = PMF.get().getPersistenceManager();
        //Key authTypeKey = KeyFactory.createKey(AuthType.class.getSimpleName(), authTypeId);
        authType = (AuthType) pm.getObjectById(AuthType.class, authTypeId);

        org.sca.calontir.cmpe.dto.AuthType at = DataTransfer.convert(authType);
        return at;
    }

    public org.sca.calontir.cmpe.dto.AuthType getAuthTypeByCode(String code) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(AuthType.class);
        query.setFilter("code == authTypeCode");
        query.declareParameters("String authTypeCode");
        List<AuthType> atList = (List<AuthType>) query.execute(code);
        AuthType at = null;
        if (atList.size() > 0) {
            at = atList.get(0);
        }
        return DataTransfer.convert(at);
    }

    public AuthType getAuthTypeDOByCode(String code) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(AuthType.class);
        query.setFilter("code == authTypeCode");
        query.declareParameters("String authTypeCode");
        List<AuthType> atList = (List<AuthType>) query.execute(code);
        AuthType at = null;
        if (atList.size() > 0) {
            at = atList.get(0);
        }
        return at;
    }

    public List<org.sca.calontir.cmpe.dto.AuthType> getAuthType() {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(AuthType.class);
        List<AuthType> atList = (List<AuthType>) query.execute();
        List<org.sca.calontir.cmpe.dto.AuthType> retList = new ArrayList<org.sca.calontir.cmpe.dto.AuthType>();
        for(AuthType at : atList) {
            org.sca.calontir.cmpe.dto.AuthType newAt = new org.sca.calontir.cmpe.dto.AuthType();
            newAt.setAuthTypeId(at.getAuthTypeId().getId());
            newAt.setCode(at.getCode());
            newAt.setDescription(at.getDescription());
            retList.add(newAt);
        }
        return retList;
    }

    public void saveAuthType(org.sca.calontir.cmpe.dto.AuthType authType) {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        AuthType at = null;
        if (authType.getAuthTypeId() != null && authType.getAuthTypeId() > 0) {
            Key authTypeKey = KeyFactory.createKey(AuthType.class.getSimpleName(), authType.getAuthTypeId());
            at = (AuthType) pm.getObjectById(AuthType.class, authTypeKey);
        }
        at = DataTransfer.convert(authType, at);
        try {
            at = pm.makePersistent(at);
        } finally {
            pm.close();
        }
    }
}
