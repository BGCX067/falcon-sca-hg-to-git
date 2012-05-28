package org.sca.calontir.cmpe.dto;

import java.io.Serializable;

/**
 *
 * @author rik
 */
public class AuthType implements Serializable {
    private Long authTypeId;
    private String code;
    private String description;
	private Integer orderValue;

    public Long getAuthTypeId() {
        return authTypeId;
    }

    public void setAuthTypeId(Long authTypeId) {
        this.authTypeId = authTypeId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public Integer getOrderValue() {
		return orderValue;
	}

	public void setOrderValue(Integer orderValue) {
		this.orderValue = orderValue;
	}
	
}
