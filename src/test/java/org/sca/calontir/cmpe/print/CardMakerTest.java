package org.sca.calontir.cmpe.print;

import com.google.appengine.api.datastore.Email;
import java.io.ByteArrayOutputStream;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sca.calontir.cmpe.dto.AuthType;
import org.sca.calontir.cmpe.dto.Authorization;
import org.sca.calontir.cmpe.dto.Fighter;
import org.sca.calontir.cmpe.dto.ScaGroup;

/**
 *
 * @author scarboroughr
 */
public class CardMakerTest {

    public CardMakerTest() {
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
     * Test of build method, of class CardMaker.
     */
//    @Test
    public void testBuild() throws Exception {
        System.out.println("build");
        ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
        CardMaker instance = new CardMaker();
        
        Fighter fighter = new Fighter();
        fighter.setScaName("Brendan Mac an tSaoir");
        fighter.setGoogleId("riksca@gmail.com");
        fighter.setScaMemberNo("38910");
        
        List <Authorization> auths = new ArrayList<Authorization>();
        Authorization auth = new Authorization();
        auth.setCode("WSH");
        auth.setDescription("Weapon and Shield");
        auth.setDate(new Date());
        auths.add(auth);

        auth = new Authorization();
        auth.setCode("SP");
        auth.setDescription("Spear");
        auth.setDate(new Date());
        auths.add(auth);

        auth = new Authorization();
        auth.setCode("TW");
        auth.setDescription("Two Weapon");
        auth.setDate(new Date());
        auths.add(auth);
        
        auth = new Authorization();
        auth.setCode("Marshal");
        auth.setDescription("Marshal");
        auth.setDate(new Date());
        auths.add(auth);
        
        fighter.setAuthorization(auths);
        
        ScaGroup group = new ScaGroup();
        group.setGroupLocation("Central");
        group.setGroupName("Forgotten Sea");
        
        fighter.setScaGroup(group);
        
        List<Fighter> data = new ArrayList<Fighter>();
        data.add(fighter);
        
        instance.build(baosPDF, data);
        FileOutputStream fos = new FileOutputStream("cardtest.pdf");
        baosPDF.writeTo(fos);
        fos.close();
    }
}
