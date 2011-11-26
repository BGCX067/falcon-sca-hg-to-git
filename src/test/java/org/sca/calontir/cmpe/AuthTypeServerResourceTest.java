package org.sca.calontir.cmpe;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.restlet.resource.ClientResource;
import static org.junit.Assert.*;
import org.sca.calontir.cmpe.common.AuthTypeResource;
import org.sca.calontir.cmpe.dto.AuthType;

/**
 *
 * @author rik
 */
public class AuthTypeServerResourceTest {
    
    public AuthTypeServerResourceTest() {
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
    public void testAuthType() {
        AuthType at = new AuthType();
    }

    /**
     * Test of retrieve method, of class AuthTypeServerResource.
     */
//    @Test
    public void testRetrieve() {
        System.out.println("retrieve");
        ClientResource clientResource = new ClientResource(
                "http://localhost:8080/ws/authType");
        AuthTypeResource instance = clientResource.wrap(AuthTypeResource.class);
        List expResult = null;
        List<AuthType> result = instance.retrieve();
        System.out.println(result);
        //for(AuthType at : result) {
//            AuthType at = result.get(1);
//            System.out.println(at.getAuthTypeId().getId());
        //}
    }

    /**
     * Test of store method, of class AuthTypeServerResource.
     */
    //@Test
    public void testStore() {
        System.out.println("store");
        AuthType authType = new AuthType();
        ClientResource clientResource = new ClientResource(
                "http://localhost:8080/authType");
        AuthTypeResource instance = clientResource.wrap(AuthTypeResource.class);
        authType.setCode("SSH");
        authType.setDescription("Sword and Shield");
        instance.store(authType);
    }
    
    //@Test
    public void testUpdate() {
        System.out.println("update");
        AuthType authType = new AuthType();
        ClientResource clientResource = new ClientResource(
                "http://localhost:8080/authType");
        AuthTypeResource instance = clientResource.wrap(AuthTypeResource.class);
        authType.setCode("GS");
        authType.setDescription("Great Sword");
        instance.store(authType);
        
    }
    /**
     * Test of remove method, of class AuthTypeServerResource.
     */
    //@Test
    public void testRemove() {
        System.out.println("remove");
        AuthTypeServerResource instance = new AuthTypeServerResource();
        instance.remove();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
