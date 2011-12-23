package org.sca.calontir.cmpe.db;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import org.sca.calontir.cmpe.data.AuthType;
import org.sca.calontir.cmpe.dto.DataTransfer;

/**
 *
 * @author rik
 */
public class AuthTypeDAO {
    public static class LocalCacheImpl extends LocalCacheAbImpl {
        private static LocalCacheImpl _instance = new LocalCacheImpl();

        public static LocalCacheImpl getInstance() {
            return _instance;
        }
    }
    static private LocalCacheImpl localCache = (LocalCacheImpl) LocalCacheImpl.getInstance();


    public org.sca.calontir.cmpe.dto.AuthType getAuthType(long authTypeId) {
        AuthType authType = (AuthType) localCache.getValue(new Long(authTypeId));
        if (authType == null) {
            PersistenceManager pm = PMF.get().getPersistenceManager();
            authType = (AuthType) pm.getObjectById(AuthType.class, authTypeId);
            localCache.put(new Long(authTypeId), authType);
        }
        org.sca.calontir.cmpe.dto.AuthType at = DataTransfer.convert(authType);
        return at;
    }

    public org.sca.calontir.cmpe.dto.AuthType getAuthTypeByCode(String code) {
        AuthType at = getAuthTypeDOByCode(code);
        return DataTransfer.convert(at);
    }

    public AuthType getAuthTypeDOByCode(String code) {
        AuthType at = (AuthType) localCache.getValue(code);
        if (at == null) {
            PersistenceManager pm = PMF.get().getPersistenceManager();
            Query query = pm.newQuery(AuthType.class);
            query.setFilter("code == authTypeCode");
            query.declareParameters("String authTypeCode");
            List<AuthType> atList = (List<AuthType>) query.execute(code);
            if (atList.size() > 0) {
                at = atList.get(0);
            }
            localCache.put(code, at);
        }
        return at;
    }

    public List<org.sca.calontir.cmpe.dto.AuthType> getAuthType() {
        List<AuthType> atList = (List<AuthType>) localCache.getValue("atlist");
        if (atList == null) {
            PersistenceManager pm = PMF.get().getPersistenceManager();
            Query query = pm.newQuery(AuthType.class);
            atList = (List<AuthType>) query.execute();
            localCache.put("atlist", atList);
        }
        List<org.sca.calontir.cmpe.dto.AuthType> retList = new ArrayList<org.sca.calontir.cmpe.dto.AuthType>();
        for (AuthType at : atList) {
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
        localCache.clear();

        AuthType at = null;
        if (authType.getAuthTypeId() != null && authType.getAuthTypeId() > 0) {
            Key authTypeKey = KeyFactory.createKey(AuthType.class.getSimpleName(), authType.getAuthTypeId());
            at = (AuthType) pm.getObjectById(AuthType.class, authTypeKey);
        } else {
            at = new AuthType();
        }
        at = DataTransfer.convert(authType, at);
        try {
            at = pm.makePersistent(at);
        } finally {
            pm.close();
        }
    }
}
