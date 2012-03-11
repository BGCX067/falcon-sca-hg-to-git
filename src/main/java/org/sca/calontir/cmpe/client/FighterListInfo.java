package org.sca.calontir.cmpe.client;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author rikscarborough
 */
public class FighterListInfo implements Serializable {

    private Long fighterId;
    private String scaName;
    private String authorizations;
    private String group;

    public String getScaName() {
        return scaName;
    }

    public void setScaName(String scaName) {
        this.scaName = scaName;
    }

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

    @Override
    public String toString() {
        return "FighterListInfo{" + "fighterId=" + fighterId + ", scaName=" + scaName + ", authorizations=" + authorizations + ", group=" + group + '}';
    }
}
