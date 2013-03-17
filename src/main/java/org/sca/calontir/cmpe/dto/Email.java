package org.sca.calontir.cmpe.dto;

import java.io.Serializable;

/**
 *
 * @author rik
 */
public class Email implements Serializable {
    private String emailAddress;
    private String type;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

	@Override
	public String toString() {
		return type + ": " + emailAddress;
	}
}
