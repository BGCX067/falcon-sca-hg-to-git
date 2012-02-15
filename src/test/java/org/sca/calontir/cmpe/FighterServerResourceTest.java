package org.sca.calontir.cmpe;

import au.com.bytecode.opencsv.CSVReader;
import java.util.ArrayList;
import java.util.List;
import org.junit.*;
import org.sca.calontir.cmpe.common.FighterResource;
import static org.junit.Assert.*;
import org.restlet.resource.ClientResource;
import org.sca.calontir.cmpe.common.AuthTypeResource;
import org.sca.calontir.cmpe.dto.Email;
import org.sca.calontir.cmpe.dto.Fighter;
import org.sca.calontir.cmpe.dto.ScaGroup;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.sca.calontir.cmpe.common.FighterStatus;
import org.sca.calontir.cmpe.common.ScaGroupResource;
import org.sca.calontir.cmpe.dto.*;

/**
 *
 * @author rik
 */
public class FighterServerResourceTest {

    public FighterServerResourceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testFighter() {
        Fighter f = new Fighter();
    }

    /**
     * Test of retrieve method, of class FighterServerResource.
     */
    //@Test
    public void testRetrieve() {
        System.out.println("retrieve");
        ClientResource clientResource = new ClientResource(
                "http://localhost:8080/fighter");
        FighterResource fighterResource = clientResource.wrap(FighterResource.class);
        List<Fighter> expResult = null;
        List<Fighter> result = fighterResource.retrieve();
        System.out.println(result);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of store method, of class FighterServerResource.
     */
    //@Test
    public void testStore() {
        System.out.println("store");
        Fighter fighter = null;
        ClientResource clientResource = new ClientResource(
                "http://localhost:8080/fighter");
        FighterResource fighterResource = clientResource.wrap(FighterResource.class);

        ClientResource authTypeClient = new ClientResource("http://localhost:8080/authType");
        AuthTypeResource authTypeResource = clientResource.wrap(AuthTypeResource.class);

        fighter = new Fighter();
        fighter.setScaName("Brendan Mac an tSaoir");
        fighter.setGoogleId("riksca@gmail.com");
        fighter.setScaMemberNo("38910");

        Email email = new Email();
        email.setEmailAddress("riksca@gmail.com");
        email.setType("personal");
        List<Email> emailList = new ArrayList<Email>();
        emailList.add(email);
        fighter.setEmail(emailList);

//        AuthType at = new AuthType();
//        at.setCode("GS");
//        at.setDescription("Great Sword");
//        
//        Authorization auth = new Authorization();
//        auth.setAuthType(at);
//        auth.setDate(new Date());
//        
//        List <Authorization> auths = new ArrayList<Authorization>();
//        auths.add(auth);

        //fighter.setAuthorization(auths);


        fighterResource.store(fighter);
    }

    //@Test
    public void testUpdate() {
        System.out.println("update");
        ClientResource clientResource = new ClientResource(
                "http://localhost:8080/fighter");
        FighterResource fighterResource = clientResource.wrap(FighterResource.class);
        List<Fighter> result = fighterResource.retrieve();
    }

    /**
     * Test of remove method, of class FighterServerResource.
     */
    //@Test
    public void testRemove() {
        System.out.println("remove");
        FighterServerResource instance = new FighterServerResource();
        instance.remove();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    private Map<String, ScaGroup> getGroupMap() {
        Map<String, ScaGroup> groups = new HashMap<String, ScaGroup>();
        ClientResource clientResource = new ClientResource(
                "http://localhost:8080/ws/scaGroup");
        ScaGroupResource groupResource = clientResource.wrap(ScaGroupResource.class);
        List groupList = groupResource.retrieve();

        for (Map groupMap : (List<Map>) groupList) {
            ScaGroup group = new ScaGroup();
            group.setGroupName(groupMap.get("groupName").toString());
            group.setGroupLocation(groupMap.get("groupLocation").toString());
            
            groups.put(group.getGroupName(), group);
        }

        return groups;
    }

    //@Test
    public void testLoadData() {
        CSVReader reader;
        Map<String, ScaGroup> groups = getGroupMap();
        ClientResource clientResource = new ClientResource(
                "http://localhost:8080/ws/fighter");
        FighterResource fighterResource = clientResource.wrap(FighterResource.class);
        try {
            reader = new CSVReader(new FileReader("2012_fighters_table.csv"));
            List<String[]> myEntries = reader.readAll();
            for (String[] e : myEntries) {
                Fighter fighter = new Fighter();
                fighter.setScaName(e[1]);
                fighter.setModernName(e[2]);
                if (e[5].equals("TRUE")) {
                    Note note = new Note();
                    note.setBody("MINOR. GET BIRTHDATE!!");
                    note.setUpdated(new Date());
                    fighter.setNote(note);
                    fighter.setDateOfBirth(new DateTime().withDate(1996, 01, 01).toDate());
                }
                if (e[6].length() > 0) {
                    Address address = new Address();
                    address.setAddress1(e[6]);
                    address.setCity(e[7]);
                    address.setState(e[8]);
                    address.setPostalCode(e[9]);
                    List<Address> addresses = new ArrayList<Address>();
                    addresses.add(address);
                    fighter.setAddress(addresses);
                }
                if (e[10].length() > 0) {
                    Phone phone = new Phone();
                    phone.setPhoneNumber(e[10]);
                    phone.setType("Home");
                    List<Phone> phones = new ArrayList<Phone>();
                    phones.add(phone);
                    fighter.setPhone(phones);
                }
                if (e[11].length() > 0) {
                    Email email = new Email();
                    email.setEmailAddress(e[11]);
                    email.setType("Home");
                    List<Email> emails = new ArrayList<Email>();
                    emails.add(email);
                    fighter.setEmail(emails);
                }
                if (e[13].length() > 0) {
                    // if matches
                    ScaGroup group = null;
                    if (groups.containsKey(e[13])) {
                        group = groups.get(e[13]);
                    } else {
                        group = groups.get("Unknown or Out of Kingdom");
                        String n = "Group in original database was " + e[13];
                        Note note = fighter.getNote();
                        if (note == null) {
                            note = new Note();
                            fighter.setNote(note);
                        } else {
                            n = note.getBody() + "\n" + n;
                        }
                        note.setBody(n);
                        note.setUpdated(new Date());
                    }

                    fighter.setScaGroup(group);
                }
                if (e[16].trim().length() > 0) {
                    String n = e[16].trim();
                    Note note = fighter.getNote();
                    if (note == null) {
                        note = new Note();
                        fighter.setNote(note);
                    } else {
                        n = note.getBody() + "\n" + n;
                    }
                    note.setBody(n);
                    note.setUpdated(new Date());
                }

                if (e[21].equals("TRUE")) {
                    fighter.setStatus(FighterStatus.INACTIVE);
                }

                List<Authorization> auths = new ArrayList<Authorization>();
                boolean isths = false;
                if (e[23].equals("TRUE")) {
                    Authorization auth = new Authorization();
                    auth.setCode("THS");
                    auths.add(auth);
                    if (e[24].length() > 0) {
                        auth.setDate(stringToDate(e[24]));
                    } else {
                        auth.setDate(new Date());
                    }
                    isths = true;
                }

                if (e[25].equals("TRUE")) {
                    Authorization auth = new Authorization();
                    auth.setCode("SP");
                    auths.add(auth);
                    if (e[26].length() > 0) {
                        auth.setDate(stringToDate(e[26]));
                    } else {
                        auth.setDate(new Date());
                    }
                }

                if (e[27].equals("TRUE")) {
                    Authorization auth = new Authorization();
                    auth.setCode("WSH");
                    auths.add(auth);
                    if (e[28].length() > 0) {
                        auth.setDate(stringToDate(e[28]));
                    } else {
                        auth.setDate(new Date());
                    }
                }

                if (e[29].equals("TRUE") && !isths) {
                    Authorization auth = new Authorization();
                    auth.setCode("THS");
                    auths.add(auth);
                    if (e[30].length() > 0) {
                        auth.setDate(stringToDate(e[30]));
                    } else {
                        auth.setDate(new Date());
                    }
                    isths = true;
                }

                if (e[33].equals("TRUE")) {
                    Authorization auth = new Authorization();
                    auth.setCode("PA");
                    auths.add(auth);
                    if (e[34].length() > 0) {
                        auth.setDate(stringToDate(e[34]));
                    } else {
                        auth.setDate(new Date());
                    }
                }

                if (e[35].equals("TRUE")) {
                    Authorization auth = new Authorization();
                    auth.setCode("TW");
                    auths.add(auth);
                    if (e[36].length() > 0) {
                        auth.setDate(stringToDate(e[36]));
                    } else {
                        auth.setDate(new Date());
                    }
                }

                if (e[37].equals("TRUE")) {
                    Authorization auth = new Authorization();
                    auth.setCode("Marshal");
                    auths.add(auth);
                    if (e[38].length() > 0) {
                        auth.setDate(stringToDate(e[37]));
                    } else {
                        auth.setDate(new Date());
                    }
                }
                fighter.setAuthorization(auths);

                System.out.println("Adding " + fighter.getScaName());
                fighterResource.store(fighter);
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    private Date stringToDate(String orig) {
        DateTime dt = null;
        if (orig.contains("-")) {
            dt = DateTimeFormat.forPattern("dd-MMM-yy").parseDateTime(orig);
        } else if (orig.contains("/")) {
            dt = DateTimeFormat.forPattern("MM/dd/yyyy").parseDateTime(orig);
        }
        if (dt == null) {
            return null;
        }
        return dt.toDate();
    }
}
