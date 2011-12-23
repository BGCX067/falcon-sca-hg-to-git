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
}
