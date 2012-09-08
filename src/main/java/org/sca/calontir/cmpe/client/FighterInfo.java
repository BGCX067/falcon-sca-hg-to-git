package org.sca.calontir.cmpe.client;

import java.io.Serializable;

/**
 *
 * @author rikscarborough
 */
public class FighterInfo  implements Serializable {
    private Long fighterId;
    private String scaName;
    private String authorizations;
    private String group;
	private String status;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
