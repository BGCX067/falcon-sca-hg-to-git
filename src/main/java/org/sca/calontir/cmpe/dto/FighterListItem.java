package org.sca.calontir.cmpe.dto;

import java.io.Serializable;
import org.sca.calontir.cmpe.common.FighterStatus;

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
    private FighterStatus status = FighterStatus.ACTIVE;
    private String role;

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

    public FighterStatus getStatus() {
        return status;
    }

    public void setStatus(FighterStatus status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public int compareTo(FighterListItem o) {
        return this.scaName.compareTo(o.getScaName());
    }
}
