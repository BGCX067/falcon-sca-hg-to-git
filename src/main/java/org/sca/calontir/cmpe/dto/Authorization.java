package org.sca.calontir.cmpe.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author rik
 */
public class Authorization implements Serializable {
    private String code;
    private String description;
    private Date date;
	private Integer orderValue;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 83 * hash + (this.code != null ? this.code.hashCode() : 0);
		hash = 83 * hash + (this.description != null ? this.description.hashCode() : 0);
		hash = 83 * hash + (this.date != null ? this.date.hashCode() : 0);
		hash = 83 * hash + (this.orderValue != null ? this.orderValue.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Authorization other = (Authorization) obj;
		if ((this.code == null) ? (other.code != null) : !this.code.equals(other.code)) {
			return false;
		}
		if ((this.description == null) ? (other.description != null) : !this.description.equals(other.description)) {
			return false;
		}
		if (this.orderValue != other.orderValue && (this.orderValue == null || !this.orderValue.equals(other.orderValue))) {
			return false;
		}
		return true;
	}
	

    @Override
    public String toString() {
        return "Authorization{" + "code=" + code + ", description=" + description + ", date=" + date + '}';
    }

	
    
}
