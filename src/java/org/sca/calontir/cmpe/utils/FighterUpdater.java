package org.sca.calontir.cmpe.utils;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.sca.calontir.cmpe.dto.AuthType;
import org.sca.calontir.cmpe.dto.Address;
import org.sca.calontir.cmpe.dto.Authorization;
import org.sca.calontir.cmpe.dto.Email;
import org.sca.calontir.cmpe.dto.Fighter;
import org.sca.calontir.cmpe.dto.Phone;
import org.sca.calontir.cmpe.dto.ScaGroup;
import org.sca.calontir.cmpe.db.AuthTypeDAO;

/**
 *
 * @author rik
 */
public class FighterUpdater {

    public static Fighter fromRequest(HttpServletRequest request, Fighter fighter) {
        Fighter f = infoFromRequest(request, fighter);
        f = authFromRequest(request, f);
        return f;
    }

    public static Fighter infoFromRequest(HttpServletRequest request, Fighter fighter) {
        fighter.setScaName(request.getParameter("scaName"));
        fighter.setScaMemberNo(request.getParameter("scaMemberNo"));
        fighter.setModernName(request.getParameter("modernName"));

        String groupStr = request.getParameter("scaGroup");
        ScaGroup group = new ScaGroup();
        group.setGroupName(groupStr);
        fighter.setScaGroup(group);

        String dob = request.getParameter("dateOfBirth");
        if (StringUtils.isNotBlank(dob)) {
            DateTimeFormatter fmt = DateTimeFormat.forPattern("MM/dd/yyyy");
            DateTime dateOfBirth = fmt.parseDateTime(dob);
            fighter.setDateOfBirth(dateOfBirth.toDate());
        }

        String[] address1 = request.getParameterValues("address1");
        String[] address2 = request.getParameterValues("address2");
        String[] city = request.getParameterValues("city");
        String[] postalCode = request.getParameterValues("postalCode");
        String[] state = request.getParameterValues("state");
        List<Address> addresses = fighter.getAddress() != null ? fighter.getAddress() : new LinkedList<Address>();
        for (int i = 0; i < address1.length; ++i) {
            Address address = addresses.size() > i ? addresses.get(i) : new Address();
            address.setAddress1(address1[i]);
            address.setAddress2(address2[i]);
            address.setCity(city[i]);
            address.setPostalCode(postalCode[i]);
            address.setState(state[i]);
            addresses.add(address);
        }
        fighter.setAddress(addresses);

        String[] phoneNumbers = request.getParameterValues("phoneNumber");
        List<Phone> phones = fighter.getPhone() != null ? fighter.getPhone() : new LinkedList<Phone>();
        for (int i = 0; i < phoneNumbers.length; ++i) {
            String phoneNumber = phoneNumbers[i];
            Phone phone = phones.size() > i ? phones.get(i) : new Phone();
            phone.setPhoneNumber(phoneNumber);
            phone.setType("home");
            phones.add(phone);
        }
        fighter.setPhone(phones);

        String[] emailArray = request.getParameterValues("email");
        List<Email> emailList = fighter.getEmail() != null ? fighter.getEmail() : new LinkedList<Email>();
        for (int i = 0; i < emailArray.length; ++i) {
            String emailStr = emailArray[i];
            Email email = emailList.size() > i ? emailList.get(i) : new Email();
            email.setEmailAddress(emailStr);
            email.setType("home");
            emailList.add(email);
        }

        fighter.setEmail(emailList);

        return fighter;
    }

    public static Fighter authFromRequest(HttpServletRequest request, Fighter fighter) {
        AuthTypeDAO atDao = new AuthTypeDAO();
        List<Authorization> authorizations = new LinkedList<Authorization>();
        String[] authCodes = request.getParameterValues("authorization");
        for (String authCode : authCodes) {
            AuthType at = atDao.getAuthTypeByCode(authCode);
            Authorization a = new Authorization();
            a.setCode(at.getCode());
            a.setDescription(at.getDescription());
            a.setDate(new Date());
            authorizations.add(a);
        }
        fighter.setAuthorization(authorizations);

        return fighter;
    }
}
