package org.sca.calontir.cmpe.dto;

/**
 *
 * @author rik
 */
public class AuthType {
    private Long authTypeId;
    private String code;
    private String description;

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
    
    
}