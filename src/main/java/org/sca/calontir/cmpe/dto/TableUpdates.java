/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.dto;

import java.util.Date;

/**
 *
 * @author rikscarborough
 */
public class TableUpdates {
    private Long tableUpdatesId;
    private String tableName;
    private Date lastUpdated;
    private Date lastDeletion;

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

    public Long getTableUpdatesId() {
        return tableUpdatesId;
    }

    public void setTableUpdatesId(Long tableUpdatesId) {
        this.tableUpdatesId = tableUpdatesId;
    }
    
    
}
