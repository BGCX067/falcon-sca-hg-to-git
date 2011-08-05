/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.user;

import com.google.appengine.api.users.User;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.sca.calontir.cmpe.common.UserRoles;
import org.sca.calontir.cmpe.dto.Fighter;

/**
 *
 * @author rik
 */
public class SecurityTest {
    
    public SecurityTest() {
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
     * Test of isRole method, of class Security.
     */
    @Test
    public void testIsRole() {
        System.out.println("isRole");
        Security instance = new Security();
        User user = new User("riksca@gmail.com", "", "riksca@gmail.com");
        Fighter fighter = new Fighter();
        fighter.setRole(UserRoles.KNIGHTS_MARSHAL);
        instance.setAeUser(user);
        instance.setUser(fighter);
        boolean expResult = false;
        boolean result = instance.isRole(UserRoles.CARD_MARSHAL);
        assertEquals(expResult, result);
    }

    /**
     * Test of isRoleOrGreater method, of class Security.
     */
    @Test
    public void testIsRoleOrGreater() {
        System.out.println("isRoleOrGreater");
        Security instance = new Security();
        User user = new User("riksca@gmail.com", "", "riksca@gmail.com");
        Fighter fighter = new Fighter();
        fighter.setRole(UserRoles.DEPUTY_EARL_MARSHAL);
        instance.setAeUser(user);
        instance.setUser(fighter);
        boolean expResult = false;
        boolean result = instance.isRoleOrGreater(UserRoles.CARD_MARSHAL);
        assertEquals(expResult, result);
        
        result = instance.isRoleOrGreater(UserRoles.KNIGHTS_MARSHAL);
        assertTrue(result);
    }

   
}
