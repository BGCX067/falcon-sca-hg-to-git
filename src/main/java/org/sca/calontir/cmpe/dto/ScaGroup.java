package org.sca.calontir.cmpe.dto;

import java.io.Serializable;

/**
 *
 * @author rik
 */
public class ScaGroup implements Serializable  {
    private String groupName;
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
}
