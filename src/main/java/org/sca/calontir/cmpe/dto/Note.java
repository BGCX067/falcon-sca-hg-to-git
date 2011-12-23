package org.sca.calontir.cmpe.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author rik
 */
public class Note implements Serializable {
    private String body;
    private Date updated;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
    
    
}
