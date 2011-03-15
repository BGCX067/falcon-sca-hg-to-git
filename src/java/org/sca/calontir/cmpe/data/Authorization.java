package org.sca.calontir.cmpe.data;

import com.google.appengine.api.datastore.Key;
import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 *
 * @author rik
 */
@PersistenceCapable(detachable = "true")
public class Authorization {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key authorizatoinId;
    @Persistent
    private AuthType authType;
    @Persistent
    private Date date;

    public AuthType getAuthType() {
        return authType;
    }

    public void setAuthType(AuthType authType) {
        this.authType = authType;
    }

    public Key getAuthorizatoinId() {
        return authorizatoinId;
    }

    public void setAuthorizatoinId(Key authorizatoinId) {
        this.authorizatoinId = authorizatoinId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
