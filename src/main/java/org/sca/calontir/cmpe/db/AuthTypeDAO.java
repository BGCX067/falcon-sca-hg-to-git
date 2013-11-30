package org.sca.calontir.cmpe.db;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.sca.calontir.cmpe.dto.AuthType;
import org.sca.calontir.cmpe.dto.DataTransfer;

/**
 *
 * @author rik
 */
public class AuthTypeDAO {

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    public static class LocalCacheImpl extends LocalCacheAbImpl {

        private static final LocalCacheImpl _instance = new LocalCacheImpl();

        public static LocalCacheImpl getInstance() {
            return _instance;
        }
    }
    private static final LocalCacheImpl localCache = (LocalCacheImpl) LocalCacheImpl.getInstance();

    public AuthType getAuthType(Key key) throws NotFoundException {
        AuthType authType = (AuthType) localCache.getValue(key.getId());
        if (authType == null) {
            Entity authEntity;
            try {
                authEntity = datastore.get(key);
            } catch (EntityNotFoundException ex) {
                Logger.getLogger(AuthTypeDAO.class.getName()).log(Level.SEVERE, null, ex);
                throw new NotFoundException("AuthType", key.getId());
            }
            authType = DataTransfer.convertAuthType(authEntity);
            localCache.put(key.getId(), authType);
        }
        return authType;
    }

    public AuthType getAuthType(long authTypeId) throws NotFoundException {
        AuthType authType = (AuthType) localCache.getValue(new Long(authTypeId));
        if (authType == null) {
            Key key = KeyFactory.createKey("AuthType", authTypeId);
            Entity authEntity;
            try {
                authEntity = datastore.get(key);
            } catch (EntityNotFoundException ex) {
                Logger.getLogger(AuthTypeDAO.class.getName()).log(Level.SEVERE, null, ex);
                throw new NotFoundException("AuthType", authTypeId);
            }
            authType = DataTransfer.convertAuthType(authEntity);
            localCache.put(new Long(authTypeId), authType);
        }
        return authType;
    }

    public Key getAuthTypeKeyByCode(String code) {
        Query.Filter codeFilter = new Query.FilterPredicate("code", Query.FilterOperator.EQUAL, code);
        Query query = new Query("AuthType").setFilter(codeFilter);
        List<Entity> atList = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
        if (!atList.isEmpty()) {
            return atList.get(0).getKey();
        }
        return null;
    }

    public AuthType getAuthTypeByCode(String code) {
        AuthType at = (AuthType) localCache.getValue(code);
        if (at == null) {
            Query.Filter codeFilter = new Query.FilterPredicate("code", Query.FilterOperator.EQUAL, code);
            Query query = new Query("AuthType").setFilter(codeFilter);
            List<Entity> atList = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
            if (!atList.isEmpty()) {
                at = DataTransfer.convertAuthType(atList.get(0));
            }
            localCache.put(code, at);
        }
        return at;
    }

    public List<AuthType> getAuthType() {
        List<AuthType> atList = (List<AuthType>) localCache.getValue("atlist");
        if (atList == null || atList.isEmpty()) {
            Query query = new Query("AuthType").addSort("orderValue");
            List<Entity> entityList = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
            atList = new ArrayList<>(entityList.size());
            for (Entity entity : entityList) {
                AuthType newAt = DataTransfer.convertAuthType(entity);
                atList.add(newAt);
            }
        }
        localCache.put("atlist", atList);
        return atList;
    }

    public Key saveAuthType(AuthType authType) {
        localCache.clear();

        Entity at = null;
        if (authType.getAuthTypeId() != null && authType.getAuthTypeId() > 0) {
            Key authTypeKey = KeyFactory.createKey(AuthType.class.getSimpleName(), authType.getAuthTypeId());
            try {
                at = datastore.get(authTypeKey);
            } catch (EntityNotFoundException ex) {
                Logger.getLogger(AuthTypeDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (at == null) {
            at = new Entity("AuthType");
        }
        at.setProperty("code", authType.getCode());
        at.setProperty("description", authType.getDescription());
        at.setProperty("orderValue", authType.getOrderValue());

        Key key = datastore.put(at);

        return key;
    }
}
