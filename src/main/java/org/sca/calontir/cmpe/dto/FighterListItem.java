package org.sca.calontir.cmpe.dto;

import java.io.Serializable;

/**
 *
 * @author rik
 */
public class FighterListItem implements Serializable, Comparable<FighterListItem> {
    private Long fighterId;
    private String scaName;
    private String authorizations;
    private String group;
    private boolean minor;

    public String getAuthorizations() {
        return authorizations;
    }

    public void setAuthorizations(String authorizations) {
        this.authorizations = authorizations;
    }

    public Long getFighterId() {
        return fighterId;
    }

    public void setFighterId(Long fighterId) {
        this.fighterId = fighterId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean isMinor() {
        return minor;
    }

    public void setMinor(boolean minor) {
        this.minor = minor;
    }

    public String getScaName() {
        return scaName;
    }

    public void setScaName(String scaName) {
        this.scaName = scaName;
    }

    @Override
    public int compareTo(FighterListItem o) {
        return this.scaName.compareTo(o.getScaName());
    }
    
}
