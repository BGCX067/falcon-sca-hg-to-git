package org.sca.calontir.cmpe.dto;

import com.google.appengine.api.datastore.Key;
import org.sca.calontir.cmpe.common.FighterStatus;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.sca.calontir.cmpe.db.AuthTypeDAO;
import org.sca.calontir.cmpe.db.ScaGroupDAO;
import org.sca.calontir.cmpe.db.TreatyDao;
import org.sca.calontir.cmpe.utils.MarshalUtils;

/**
 *
 * @author rik
 */
public class DataTransfer {

    public static Fighter convert(org.sca.calontir.cmpe.data.Fighter fighterDO) {
        Fighter fighter = new Fighter();
        if (fighterDO.getFighterId() != null) {
            fighter.setFighterId(fighterDO.getFighterId().getId());
        }
        fighter.setScaName(fighterDO.getScaName());
        fighter.setScaMemberNo(fighterDO.getScaMemberNo());
        fighter.setModernName(fighterDO.getModernName());
        fighter.setDateOfBirth(fighterDO.getDateOfBirth());
        fighter.setGoogleId(fighterDO.getGoogleId());
        if (fighterDO.getEmail() != null) {
            List<Email> emails = new ArrayList<Email>();
            for (org.sca.calontir.cmpe.data.Email email : fighterDO.getEmail()) {
                emails.add(convert(email));
            }
            fighter.setEmail(emails);
        }
        if (fighterDO.getAddress() != null) {
            List<Address> addresses = new ArrayList<Address>();
            for (org.sca.calontir.cmpe.data.Address address : fighterDO.getAddress()) {
                addresses.add(convert(address));
            }
            fighter.setAddress(addresses);
        }
        if (fighterDO.getPhone() != null) {
            List<Phone> phones = new ArrayList<Phone>();
            for (org.sca.calontir.cmpe.data.Phone phone : fighterDO.getPhone()) {
                phones.add(convert(phone));
            }
            fighter.setPhone(phones);
        }
        if (fighterDO.getAuthorization() != null) {
            List<Authorization> authorizations = new ArrayList<Authorization>();
            for (org.sca.calontir.cmpe.data.Authorization authorization : fighterDO.getAuthorization()) {
                authorizations.add(convert(authorization));
            }
            fighter.setAuthorization(authorizations);
        }

        if (fighterDO.getScaGroup() != null) {
            ScaGroupDAO groupDao = new ScaGroupDAO();
            fighter.setScaGroup(groupDao.getScaGroup(fighterDO.getScaGroup().getId()));
        }

        fighter.setGoogleId(fighterDO.getGoogleId());

        if (fighterDO.getRole() != null) {
            fighter.setRole(fighterDO.getRole());
        }

        if (StringUtils.isBlank(fighterDO.getStatus())) { // for now set to Active
            fighter.setStatus(FighterStatus.ACTIVE);
        } else {
            fighter.setStatus(FighterStatus.valueOf(fighterDO.getStatus()));
        }

        if (fighterDO.getTreatyKey() != null) {
            TreatyDao treatyDao = new TreatyDao();
            Treaty treaty = treatyDao.getTreaty(fighterDO.getTreatyKey().getId());
            fighter.setTreaty(treaty);
        }

        if (fighterDO.getNote() != null) {
            fighter.setNote(convert(fighterDO.getNote()));
        }

        return fighter;
    }

    public static Note convert(org.sca.calontir.cmpe.data.Note noteDO) {
        Note note = new Note();
        note.setBody(noteDO.getBody());
        note.setUpdated(noteDO.getUpdated());
        return note;
    }

    public static Treaty convert(org.sca.calontir.cmpe.data.Treaty treatyDO) {
        Treaty treaty = new Treaty();
        List<Fighter> fighters = new ArrayList<Fighter>();
        for (org.sca.calontir.cmpe.data.Fighter fDO : treatyDO.getFighters()) {
            fighters.add(convert(fDO));
        }
        treaty.setFighters(fighters);
        treaty.setName(treatyDO.getName());
        treaty.setTreatyId(treatyDO.getTreatyId().getId());
        return treaty;
    }

    public static Email convert(org.sca.calontir.cmpe.data.Email emailDO) {
        Email email = new Email();
        email.setEmailAddress(emailDO.getEmailAddress());
        email.setType(emailDO.getType());
        return email;
    }

    public static Address convert(org.sca.calontir.cmpe.data.Address addressDO) {
        Address address = new Address();
        address.setAddress1(addressDO.getAddress1());
        address.setAddress2(addressDO.getAddress2());
        address.setCity(addressDO.getCity());
        address.setDistrict(addressDO.getDistrict());
        address.setPostalCode(addressDO.getPostalCode());
        address.setState(addressDO.getState());
        address.setType(addressDO.getType());
        return address;
    }

    public static Phone convert(org.sca.calontir.cmpe.data.Phone phoneDO) {
        Phone phone = new Phone();
        phone.setPhoneNumber(phoneDO.getPhoneNumber());
        phone.setType(phoneDO.getType());
        return phone;
    }

    public static Authorization convert(org.sca.calontir.cmpe.data.Authorization authorizationDO) {
        Authorization authorization = new Authorization();
        AuthTypeDAO authTypeDao = new AuthTypeDAO();
        AuthType at = authTypeDao.getAuthType(authorizationDO.getAuthType().getId());
        authorization.setCode(at.getCode());
        authorization.setDescription(at.getDescription());
        authorization.setDate(authorizationDO.getDate());
        return authorization;
    }

    public static ScaGroup convert(org.sca.calontir.cmpe.data.ScaGroup scaGroupDO) {
        ScaGroup scaGroup = new ScaGroup();
        scaGroup.setGroupName(scaGroupDO.getGroupName());
        scaGroup.setGroupLocation(scaGroup.getGroupLocation());
        return scaGroup;
    }

    public static org.sca.calontir.cmpe.data.Fighter convert(Fighter fighter, org.sca.calontir.cmpe.data.Fighter fighterDO) {
        if (fighterDO == null) {
            fighterDO = new org.sca.calontir.cmpe.data.Fighter();
        }
        fighterDO.setScaName(fighter.getScaName());
        fighterDO.setScaMemberNo(fighter.getScaMemberNo());
        fighterDO.setModernName(fighter.getModernName());
        fighterDO.setDateOfBirth(fighter.getDateOfBirth());
        fighterDO.setGoogleId(fighter.getGoogleId());
        if (fighter.getEmail() != null) {
            List<org.sca.calontir.cmpe.data.Email> emails = new ArrayList<org.sca.calontir.cmpe.data.Email>();
            for (int i = 0; i < fighter.getEmail().size(); ++i) {
                Email email = fighter.getEmail().get(i);
                if (fighterDO.getEmail() != null && i < fighterDO.getEmail().size()) {
                    org.sca.calontir.cmpe.data.Email origEmail = fighterDO.getEmail().get(i);
                    emails.add(convert(email, origEmail));
                } else {
                    emails.add(convert(email, null));
                }
            }
            fighterDO.setEmail(emails);
        }
        if (fighter.getAddress() != null) {
            List<org.sca.calontir.cmpe.data.Address> addresses = new ArrayList<org.sca.calontir.cmpe.data.Address>();
            for (int i = 0; i < fighter.getAddress().size(); ++i) {
                Address address = fighter.getAddress().get(i);
                if (fighterDO.getAddress() != null && i < fighterDO.getAddress().size()) {
                    org.sca.calontir.cmpe.data.Address origAddress = fighterDO.getAddress().get(i);
                    addresses.add(convert(address, origAddress));
                } else {
                    addresses.add(convert(address, null));
                }
            }
            fighterDO.setAddress(addresses);
        }
        if (fighter.getPhone() != null) {
            List<org.sca.calontir.cmpe.data.Phone> phones = new ArrayList<org.sca.calontir.cmpe.data.Phone>();
            for (int i = 0; i < fighter.getPhone().size(); ++i) {
                Phone phone = fighter.getPhone().get(i);
                if (fighterDO.getPhone() != null && i < fighterDO.getPhone().size()) {
                    org.sca.calontir.cmpe.data.Phone origPhone = fighterDO.getPhone().get(i);
                    phones.add(convert(phone, origPhone));
                } else {
                    phones.add(convert(phone, null));
                }
            }
            fighterDO.setPhone(phones);
        }
        if (fighter.getAuthorization() != null) {
            List<org.sca.calontir.cmpe.data.Authorization> authorizations = new ArrayList<org.sca.calontir.cmpe.data.Authorization>();
            for (int i = 0; i < fighter.getAuthorization().size(); ++i) {
                Authorization authorization = fighter.getAuthorization().get(i);
//                if (fighterDO.getAuthorization() != null && i < fighterDO.getAuthorization().size()) {
//                    org.sca.calontir.cmpe.data.Authorization origAuthorization = fighterDO.getAuthorization().get(i);
//                    authorizations.add(convert(authorization, origAuthorization));
//                } else {
                authorizations.add(convert(authorization, null));
//                }
            }
            fighterDO.setAuthorization(authorizations);
        }

        if (fighter.getScaGroup() != null) {
            org.sca.calontir.cmpe.data.ScaGroup scaGroupDO = lookup(fighter.getScaGroup());
            fighterDO.setScaGroup(scaGroupDO.getScaGroupId());
        }

        fighterDO.setGoogleId(fighter.getGoogleId());

        if (fighter.getRole() != null) {
            fighterDO.setRole(fighter.getRole());
        }

        fighterDO.setStatus(fighter.getStatus().toString());

        if (fighter.getTreaty() != null) {
            TreatyDao treatyDao = new TreatyDao();
            Key k = null;
            if (fighter.getTreaty().getTreatyId() != null) {
                k = treatyDao.getTreatyId(fighter.getTreaty().getTreatyId());
            } else {
                List<org.sca.calontir.cmpe.data.Treaty> treaties = treatyDao.getTreaties();
                if (treaties.isEmpty()) {
                    org.sca.calontir.cmpe.data.Treaty t = new org.sca.calontir.cmpe.data.Treaty();
                    t.setName("Treaty");
                    treatyDao.saveTreaty(t);
                } else {
                    k = treaties.get(0).getTreatyId();
                }
            }

            fighterDO.setTreatyKey(k);
        } else {
            fighterDO.setTreatyKey(null);
        }

        if (fighter.getNote() != null) {
//            System.err.println("setting note to " + fighter.getNote().getBody());
            org.sca.calontir.cmpe.data.Note note = fighterDO.getNote();
            fighterDO.setNote(convert(fighter.getNote(), note));
        }

//        System.out.println("fighter note set to " + fighterDO.getNote().getBody());

        return fighterDO;
    }

    public static org.sca.calontir.cmpe.data.Note convert(Note note, org.sca.calontir.cmpe.data.Note origNote) {
        if (origNote == null) {
            origNote = new org.sca.calontir.cmpe.data.Note();
        }
        if (origNote.getBody() == null || !origNote.getBody().equals(note.getBody())) {
            origNote.setBody(note.getBody());
            origNote.setUpdated(note.getUpdated());
        }
        return origNote;
    }

    public static org.sca.calontir.cmpe.data.Email convert(Email email, org.sca.calontir.cmpe.data.Email origEmail) {
        if (origEmail == null) {
            origEmail = new org.sca.calontir.cmpe.data.Email();
        }
        origEmail.setEmailAddress(email.getEmailAddress());
        origEmail.setType(email.getType());
        return origEmail;
    }

    public static org.sca.calontir.cmpe.data.Address convert(Address address, org.sca.calontir.cmpe.data.Address addressDO) {
        if (addressDO == null) {
            addressDO = new org.sca.calontir.cmpe.data.Address();
        }
        addressDO.setAddress1(address.getAddress1());
        addressDO.setAddress2(address.getAddress2());
        addressDO.setCity(address.getCity());
        addressDO.setDistrict(address.getDistrict());
        addressDO.setPostalCode(address.getPostalCode());
        addressDO.setState(address.getState());
        addressDO.setType(address.getType());
        return addressDO;
    }

    public static org.sca.calontir.cmpe.data.Phone convert(Phone phone, org.sca.calontir.cmpe.data.Phone phoneDO) {
        if (phoneDO == null) {
            phoneDO = new org.sca.calontir.cmpe.data.Phone();
        }
        phoneDO.setPhoneNumber(phone.getPhoneNumber());
        phoneDO.setType(phone.getType());
        return phoneDO;
    }

    public static org.sca.calontir.cmpe.data.Authorization convert(Authorization authorization, org.sca.calontir.cmpe.data.Authorization authorizationDO) {
        if (authorizationDO == null) {
            authorizationDO = new org.sca.calontir.cmpe.data.Authorization();
        }
        AuthTypeDAO authTypeDao = new AuthTypeDAO();
        org.sca.calontir.cmpe.data.AuthType at = authTypeDao.getAuthTypeDOByCode(authorization.getCode());
        authorizationDO.setAuthType(at.getAuthTypeId());
        authorizationDO.setDate(authorizationDO.getDate());
        return authorizationDO;
    }

    public static org.sca.calontir.cmpe.data.ScaGroup convert(ScaGroup scaGroup, org.sca.calontir.cmpe.data.ScaGroup scaGroupDO) {
        if (scaGroupDO == null) {
            scaGroupDO = new org.sca.calontir.cmpe.data.ScaGroup();
        }
        scaGroupDO.setGroupName(scaGroup.getGroupName());
        scaGroupDO.setGroupLocation(scaGroup.getGroupLocation());
        return scaGroupDO;
    }

    public static org.sca.calontir.cmpe.data.ScaGroup lookup(ScaGroup scaGroup) {
        ScaGroupDAO groupDAO = new ScaGroupDAO();
        org.sca.calontir.cmpe.data.ScaGroup scaGroupDO = groupDAO.getScaGroupDOByName(scaGroup.getGroupName());
        return scaGroupDO;
    }

    public static AuthType convert(org.sca.calontir.cmpe.data.AuthType authType) {
        AuthType at = new AuthType();
        at.setAuthTypeId(authType.getAuthTypeId().getId());
        at.setCode(authType.getCode());
        at.setDescription(authType.getDescription());
        return at;
    }

    public static org.sca.calontir.cmpe.data.AuthType convert(AuthType authType, org.sca.calontir.cmpe.data.AuthType at) {
        at.setCode(authType.getCode());
        at.setDescription(authType.getDescription());
        return at;
    }

    public static FighterListItem convertToListItem(org.sca.calontir.cmpe.data.Fighter f) {
        FighterListItem fli = new FighterListItem();
        if (f.getFighterId() != null) {
            fli.setFighterId(f.getFighterId().getId());
        }
        fli.setScaName(f.getScaName());
        if (f.getScaGroup() != null) {
            ScaGroupDAO groupDao = new ScaGroupDAO();
            ScaGroup group = groupDao.getScaGroup(f.getScaGroup().getId());
            fli.setGroup(group.getGroupName());
        }
        if (f.getAuthorization() != null) {
            List<Authorization> authorizations = new ArrayList<Authorization>();
            for (org.sca.calontir.cmpe.data.Authorization authorization : f.getAuthorization()) {
                authorizations.add(convert(authorization));
            }
            String s = MarshalUtils.getAuthsAsString(authorizations);
            fli.setAuthorizations(s);
        }

        fli.setMinor(MarshalUtils.isMinor(f.getDateOfBirth()));
        return fli;
    }
}
