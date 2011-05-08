/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sca.calontir.cmpe;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.sca.calontir.cmpe.common.FighterResource;
import static org.junit.Assert.*;
import org.restlet.resource.ClientResource;
import org.sca.calontir.cmpe.common.AuthTypeResource;
import org.sca.calontir.cmpe.data.Email;
import org.sca.calontir.cmpe.data.Fighter;

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

}