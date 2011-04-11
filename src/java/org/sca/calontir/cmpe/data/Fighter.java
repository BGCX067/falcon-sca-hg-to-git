package org.sca.calontir.cmpe.data;

import com.google.appengine.api.datastore.Key;
import java.util.List;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 *
 * @author rik
 */
@PersistenceCapable(detachable="true")
public class Fighter {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key fighterId;

    @Persistent
    private String scaName;

    @Persistent
    private String scaMemberNo;

    @Persistent
    private String googleId;

    @Persistent
    private List<Email> email;

    @Persistent
    private List<Address> address;

    @Persistent
    private List<Phone> phone;

    @Persistent
    private List<Authorization> authorization;

    @Persistent
    private ScaGroup scaGroup;

    public Key getFighterId() {
        return fighterId;
    }

    public void setFighterId(Key fighterId) {
        this.fighterId = fighterId;
    }


    public String getScaName() {
        return scaName;
    }

    public void setScaName(String scaName) {
        this.scaName = scaName;
    }

    public List<Email> getEmail() {
        return email;
    }

    public void setEmail(List<Email> email) {
        this.email = email;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getScaMemberNo() {
        return scaMemberNo;
    }

    public void setScaMemberNo(String scaMemberNo) {
        this.scaMemberNo = scaMemberNo;
    }

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }

    public List<Authorization> getAuthorization() {
        return authorization;
    }

    public void setAuthorization(List<Authorization> authorization) {
        this.authorization = authorization;
    }

    public List<Phone> getPhone() {
        return phone;
    }

    public void setPhone(List<Phone> phone) {
        this.phone = phone;
    }

    public ScaGroup getScaGroup() {
        return scaGroup;
    }

    public void setScaGroup(ScaGroup scaGroup) {
        this.scaGroup = scaGroup;
    }

    
    
}
