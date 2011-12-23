package org.sca.calontir.cmpe.data;

import com.google.appengine.api.datastore.Key;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 *
 * @author rik
 */
@PersistenceCapable()
public class ScaGroup {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key scaGroupId;

    @Persistent
    private String groupName;

    @Persistent
    private String groupLocation;

    public String getGroupLocation() {
        return groupLocation;
    }

    public void setGroupLocation(String groupLocation) {
        this.groupLocation = groupLocation;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Key getScaGroupId() {
        return scaGroupId;
    }

    public void setScaGroupId(Key scaGroupId) {
        this.scaGroupId = scaGroupId;
    }
    
}
