package org.sca.calontir.cmpe.dto;

import java.io.Serializable;
import java.util.List;
import org.sca.calontir.cmpe.common.FighterStatus;
import org.sca.calontir.cmpe.common.UserRoles;

/**
 *
 * @author rik
 */
public class Fighter implements Serializable {

    private Long fighterId;
    private String scaName;
    private String scaMemberNo;
    private String membershipExpires;
    private String modernName;
    private String dateOfBirth;
    private String googleId;
    private List<Email> email;
    private List<Address> address;
    private List<Phone> phone;
    private List<Authorization> authorization;
    private ScaGroup scaGroup;
    private UserRoles role;
    private FighterStatus status = FighterStatus.ACTIVE;
    private Treaty treaty;
    private Note note;

    public Address getPrimeAddress() {
        if (address == null || address.isEmpty()) {
            return null;
        }
        return address.get(0);
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Email getPrimeEmail() {
        if (email == null || email.isEmpty()) {
            return null;
        }
        return email.get(0);
    }

    public List<Email> getEmail() {
        return email;
    }

    public void setEmail(List<Email> email) {
        this.email = email;
    }

    public Long getFighterId() {
        return fighterId;
    }

    public void setFighterId(Long fighterId) {
        this.fighterId = fighterId;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getModernName() {
        return modernName;
    }

    public void setModernName(String modernName) {
        this.modernName = modernName;
    }

    public Phone getPrimePhone() {
        if (phone == null || phone.isEmpty()) {
            return null;
        }
        return phone.get(0);
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

    public String getScaMemberNo() {
        return scaMemberNo;
    }

    public void setScaMemberNo(String scaMemberNo) {
        this.scaMemberNo = scaMemberNo;
    }

    public String getMembershipExpires() {
        return membershipExpires;
    }

    public void setMembershipExpires(String membershipExpires) {
        this.membershipExpires = membershipExpires;
    }

    public String getScaName() {
        return scaName;
    }

    public void setScaName(String scaName) {
        this.scaName = scaName;
    }

    public UserRoles getRole() {
        return role;
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }

    public FighterStatus getStatus() {
        return status;
    }

    public void setStatus(FighterStatus status) {
        this.status = status;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public Treaty getTreaty() {
        return treaty;
    }

    public void setTreaty(Treaty treaty) {
        this.treaty = treaty;
    }

    @Override
    public String toString() {
        return "Fighter{" + "fighterId=" + fighterId + ", scaName=" + scaName + ", scaMemberNo=" + scaMemberNo + ", modernName=" + modernName + ", dateOfBirth=" + dateOfBirth + ", googleId=" + googleId + ", email=" + email + ", address=" + address + ", phone=" + phone + ", authorization=" + authorization + ", scaGroup=" + scaGroup + ", role=" + role + ", status=" + status + ", treaty=" + treaty + ", note=" + note + '}';
    }
}
