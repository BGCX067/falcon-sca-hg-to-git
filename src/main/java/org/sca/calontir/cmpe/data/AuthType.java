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
@PersistenceCapable(detachable = "true")
public class AuthType {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key authTypeId;

    @Persistent
    private String code;

    @Persistent
    private String description;

	@Persistent
	private Integer orderValue;

    public Key getAuthTypeId() {
        return authTypeId;
    }

    public void setAuthTypeId(Key authTypeId) {
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
