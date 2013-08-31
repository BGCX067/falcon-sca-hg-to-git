package org.sca.calontir.cmpe.dto;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.sca.calontir.cmpe.common.FighterStatus;
import org.sca.calontir.cmpe.common.UserRoles;
import org.sca.calontir.cmpe.db.AuthTypeDAO;
import org.sca.calontir.cmpe.db.NotFoundException;
import org.sca.calontir.cmpe.db.ScaGroupDAO;
import org.sca.calontir.cmpe.db.TreatyDao;
import org.sca.calontir.cmpe.utils.MarshalUtils;

/**
 *
 * @author rik
 */
public class DataTransfer {

    public static Fighter convert(Entity fighterEntity, DatastoreService datastore) {
        Fighter fighter = new Fighter();
        if (fighterEntity.getKey() != null) {
            fighter.setFighterId(fighterEntity.getKey().getId());
        }
        fighter.setScaName((String) fighterEntity.getProperty("scaName"));
        fighter.setScaMemberNo((String) fighterEntity.getProperty("scaMemberNo"));
        fighter.setModernName((String) fighterEntity.getProperty("modernName"));
        if (fighterEntity.hasProperty("kingdom")) {
            try {
                Kingdom kingdom = Kingdom.valueOf("kingdom");
                fighter.setKingdom(kingdom);
            } catch (IllegalArgumentException e) {
            }
        }
        if (fighterEntity.hasProperty("membershipExpires")) {
            Date expires = (Date) fighterEntity.getProperty("membershipExpires");
            if (expires != null) {
                String dt = new DateTime(expires.getTime()).toString("MM/dd/yyyy");
                fighter.setMembershipExpires(dt);
            }
        }
        if (fighterEntity.hasProperty("dateOfBirth")) {
            Date dob = (Date) fighterEntity.getProperty("dateOfBirth");
            if (dob != null) {
                String dt = new DateTime(dob.getTime()).toString("MM/dd/yyyy");
                fighter.setDateOfBirth(dt);
            }
        }
        fighter.setGoogleId((String) fighterEntity.getProperty("googleId"));

        Query emailQuery = new Query("Email").setAncestor(fighterEntity.getKey());
        List<Entity> emailEntities = datastore.prepare(emailQuery).asList(FetchOptions.Builder.withDefaults());
        List<Email> emails = new ArrayList<>();
        for (Entity emailEntity : emailEntities) {
            Email email = new Email();
            email.setEmailAddress((String) emailEntity.getProperty("emailAddress"));
            email.setType((String) emailEntity.getProperty("type"));
            emails.add(email);
        }
        fighter.setEmail(emails);

        Query addressQuery = new Query("Address").setAncestor(fighterEntity.getKey());
        List<Entity> addressEntities = datastore.prepare(addressQuery).asList(FetchOptions.Builder.withDefaults());
        List<Address> addresses = new ArrayList<>();
        for (Entity addressEntity : addressEntities) {
            Address address = new Address();
            address.setAddress1((String) addressEntity.getProperty("address1"));
            address.setAddress2((String) addressEntity.getProperty("address2"));
            address.setCity((String) addressEntity.getProperty("city"));
            address.setDistrict((String) addressEntity.getProperty("district"));
            address.setPostalCode((String) addressEntity.getProperty("postalCode"));
            address.setState((String) addressEntity.getProperty("state"));
            address.setType((String) addressEntity.getProperty("type"));
            addresses.add(address);
        }
        fighter.setAddress(addresses);

        Query phoneQuery = new Query("Phone").setAncestor(fighterEntity.getKey());
        List<Entity> phoneEntities = datastore.prepare(phoneQuery).asList(FetchOptions.Builder.withDefaults());
        List<Phone> phones = new ArrayList<>();
        for (Entity phoneEntity : phoneEntities) {
            Phone phone = new Phone();
            phone.setPhoneNumber((String) phoneEntity.getProperty("phoneNumber"));
            phone.setType((String) phoneEntity.getProperty("type"));
            phones.add(phone);
        }
        fighter.setPhone(phones);

        Query authQuery = new Query("Authorization").setAncestor(fighterEntity.getKey());
        List<Entity> authEntities = datastore.prepare(authQuery).asList(FetchOptions.Builder.withDefaults());
        List<Authorization> authorizations = new ArrayList<>();
        AuthTypeDAO authTypeDao = new AuthTypeDAO();
        for (Entity authorizationEntity : authEntities) {
            Key authTypeKey = (Key) authorizationEntity.getProperty("authType");
            try {
                if (authTypeKey != null) {
                    AuthType authType = authTypeDao.getAuthType(authTypeKey);
                    Authorization authorization = new Authorization();
                    authorization.setCode(authType.getCode());
                    authorization.setDate((Date) authorizationEntity.getProperty("date"));
                    authorization.setDescription(authType.getDescription());
                    authorization.setOrderValue(authType.getOrderValue());
                    authorizations.add(authorization);
                }
            } catch (NotFoundException ex) {
                Logger.getLogger(DataTransfer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        fighter.setAuthorization(authorizations);

        if (fighterEntity.hasProperty("scaGroup")) {
            try {
                Entity scaGroupEntity = datastore.get((Key) fighterEntity.getProperty("scaGroup"));
                ScaGroup scaGroup = new ScaGroup();
                scaGroup.setGroupLocation((String) scaGroupEntity.getProperty("groupLocation"));
                scaGroup.setGroupName((String) scaGroupEntity.getProperty("groupName"));
                fighter.setScaGroup(scaGroup);
            } catch (EntityNotFoundException ex) {
                Logger.getLogger(DataTransfer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        fighter.setGoogleId((String) fighterEntity.getProperty("googleId"));

        if (fighterEntity.hasProperty("role")) {
            String role = (String) fighterEntity.getProperty("role");
            if (role != null) {
                fighter.setRole(UserRoles.valueOf(role));
            }
        }

        if (fighterEntity.hasProperty("status")) {
            String status = (String) fighterEntity.getProperty("status");
            if (status == null) { // for now set to Active
                fighter.setStatus(FighterStatus.ACTIVE);
            } else {
                fighter.setStatus(FighterStatus.valueOf(status));
            }
        } else {
            fighter.setStatus(FighterStatus.ACTIVE);
        }

        if (fighterEntity.hasProperty("treatyKey")) {
            TreatyDao treatyDao = new TreatyDao();
            Key treatyKey = (Key) fighterEntity.getProperty("treatyKey");
            if (treatyKey != null) {
                Treaty treaty = treatyDao.getTreaty(treatyKey.getId());
                fighter.setTreaty(treaty);
            }
        }

        Query noteQuery = new Query("Note").setAncestor(fighterEntity.getKey());
        List<Entity> noteEntities = datastore.prepare(noteQuery).asList(FetchOptions.Builder.withDefaults());
        if (!noteEntities.isEmpty()) {
            Note note = new Note();
            Entity noteEntity = noteEntities.get(0);
            note.setBody((String) noteEntity.getProperty("body"));
            note.setUpdated((Date) noteEntity.getProperty("updated"));
            fighter.setNote(note);
        }

        return fighter;
    }

    public static Treaty entityToTreaty(Entity entity) {
        Treaty treaty = new Treaty();
        treaty.setName((String) entity.getProperty("name"));
        treaty.setTreatyId(entity.getKey().getId());
        return treaty;
    }

    public static Entity treatyToEntity(Treaty treaty) {
        Entity entity;
        if (treaty.getTreatyId() == null || treaty.getTreatyId() == 0) {
            entity = new Entity("Entity");
        } else {
            entity = new Entity("Entity", treaty.getTreatyId());
        }
        entity.setProperty("name", treaty.getName());
        return entity;
    }

    public static Entity convert(Fighter fighter, Entity entity) {
        if (entity == null) {
            entity = new Entity("Fighter");
        }
        entity.setProperty("scaName", fighter.getScaName());
        entity.setProperty("scaMemberNo", fighter.getScaMemberNo());
        entity.setProperty("modernName", fighter.getModernName());
        if (fighter.getKingdom() != null) {
            entity.setProperty("kingdom", fighter.getKingdom().toString());
        }
        if (StringUtils.isNotBlank(fighter.getMembershipExpires())) {
            DateTime dt = DateTimeFormat.forPattern("MM/dd/yyyy").parseDateTime(fighter.getMembershipExpires());
            entity.setProperty("membershipExpires", dt.toDate());
        } else {
            entity.removeProperty("membershipExpires");
        }
        if (StringUtils.isNotBlank(fighter.getDateOfBirth())) {
            DateTime dt = DateTimeFormat.forPattern("MM/dd/yyyy").parseDateTime(fighter.getDateOfBirth());
            entity.setProperty("dateOfBirth", dt.toDate());
        } else {
            entity.removeProperty("dateOfBirth");
        }
        entity.setProperty("googleId", fighter.getGoogleId());

        if (fighter.getScaGroup() != null) {
            ScaGroupDAO groupDAO = new ScaGroupDAO();
            entity.setProperty("scaGroup", groupDAO.getScaGroupKey(fighter.getScaGroup().getGroupName()));
        }

        if (fighter.getRole() != null) {
            entity.setProperty("role", fighter.getRole().toString());
        }

        entity.setProperty("status", fighter.getStatus().toString());

        if (fighter.getTreaty() != null) {
            TreatyDao treatyDao = new TreatyDao();
            Key k = null;
            if (fighter.getTreaty().getTreatyId() != null) {
                k = treatyDao.getTreatyId(fighter.getTreaty().getTreatyId());
            }

            entity.setProperty("treatyKey", k);
        } else {
            entity.removeProperty("treatyKey");
        }

        return entity;
    }

    public static AuthType convertAuthType(Entity authType) {
        AuthType at = new AuthType();
        at.setAuthTypeId(authType.getKey().getId());
        at.setCode((String) authType.getProperty("code"));
        at.setDescription((String) authType.getProperty("description"));
        Long orderValue = (Long) authType.getProperty("orderValue");
        at.setOrderValue(orderValue != null && orderValue > 0 ? orderValue : getOrderFromAuthCode(at.getCode()));
        return at;
    }

    //TODO: Temporary until order value is added to the database.
    private static int getOrderFromAuthCode(String code) {
        int retValue;
        if (code.equalsIgnoreCase("WSH")) {
            retValue = 1;
        } else if (code.equalsIgnoreCase("TW")) {
            retValue = 2;
        } else if (code.equalsIgnoreCase("THS")) {
            retValue = 3;
        } else if (code.equalsIgnoreCase("PA")) {
            retValue = 4;
        } else if (code.equalsIgnoreCase("SP")) {
            retValue = 5;
        } else if (code.equalsIgnoreCase("Marshal")) {
            retValue = 6;
        } else {
            retValue = 9;
        }

        return retValue;
    }

    public static FighterListItem convertToListItem(Entity f, DatastoreService datastore) {
        FighterListItem fli = new FighterListItem();
        fli.setFighterId(f.getKey().getId());
        fli.setScaName((String) f.getProperty("scaName"));
        if (f.hasProperty("scaGroup")) {
            Entity scaGroupEntity;
            try {
                scaGroupEntity = datastore.get((Key) f.getProperty("scaGroup"));
                fli.setGroup((String) scaGroupEntity.getProperty("groupName"));
            } catch (EntityNotFoundException ex) {
                Logger.getLogger(DataTransfer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Query query = new Query("Authorization").setAncestor(f.getKey());
        List<Entity> authorizations = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
        List<Authorization> auths = new ArrayList<>();
        AuthTypeDAO authTypeDao = new AuthTypeDAO();
        for (Entity authEntity : authorizations) {
            Key authTypeKey = (Key) authEntity.getProperty("authType");
            try {
                if (authTypeKey != null) {
                    AuthType authType = authTypeDao.getAuthType(authTypeKey);
                    Authorization authorization = new Authorization();
                    authorization.setCode(authType.getCode());
                    authorization.setDate((Date) authEntity.getProperty("date"));
                    authorization.setDescription(authType.getDescription());
                    authorization.setOrderValue(authType.getOrderValue());
                    auths.add(authorization);
                }
            } catch (NotFoundException ex) {
                Logger.getLogger(DataTransfer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        String s = MarshalUtils.getAuthsAsString(auths);
        fli.setAuthorizations(s);

        fli.setMinor(MarshalUtils.isMinor((Date) f.getProperty("dateOfBirth")));
        fli.setStatus(FighterStatus.valueOf((String) f.getProperty("status")));
        fli.setRole((String) f.getProperty("role"));
        return fli;
    }
}
