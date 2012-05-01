package org.sca.calontir.cmpe.data;

import com.google.appengine.api.datastore.Key;
import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 *
 * @author rikscarborough
 */
@PersistenceCapable(detachable = "true")
public class TableUpdates {
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key tableUpdatesId;
    @Persistent
    private String tableName;
    @Persistent
    private Date lastUpdated;
    @Persistent
    private Date lastDeletion;

    public Key getTableUpdatesId() {
        return tableUpdatesId;
    }

    public void setTableUpdatesId(Key tableUpdatesId) {
        this.tableUpdatesId = tableUpdatesId;
    }

    
    public Date getLastDeletion() {
        return lastDeletion;
    }

    public void setLastDeletion(Date lastDeletion) {
        this.lastDeletion = lastDeletion;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    
}
