package org.sca.calontir.cmpe.print;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.joda.time.DateTime;
import org.junit.*;
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
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testBuild() throws Exception {
        System.out.println("build");
        ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
        CardMaker instance = new CardMaker();

        Fighter fighter = new Fighter();
        fighter.setScaName("His Lordship Brendan Mac an tSaoir, Squire of Rorik Galbraith, Deputy Kingdom Seneschal, Writer of SCA software, Nice Guy");
        fighter.setGoogleId("riksca@gmail.com");
        fighter.setScaMemberNo("38910");
        fighter.setModernName("Rik Scarborough");

        int i = 0;
        List<Authorization> auths = new ArrayList<>();
        Authorization auth = new Authorization();
        auth.setCode("WSH");
        auth.setDescription("Weapon and Shield");
        auth.setDate(new Date());
        auth.setOrderValue(new Long(++i));
        auths.add(auth);

        auth = new Authorization();
        auth.setCode("TW");
        auth.setDescription("Two Weapons");
        auth.setDate(new Date());
        auth.setOrderValue(new Long(++i));
        auths.add(auth);

        auth = new Authorization();
        auth.setCode("THS");
        auth.setDescription("Two Handed Sword");
        auth.setDate(new Date());
        auth.setOrderValue(new Long(++i));
        auths.add(auth);

        auth = new Authorization();
        auth.setCode("PA");
        auth.setDescription("Pole Arm");
        auth.setDate(new Date());
        auth.setOrderValue(new Long(++i));
        auths.add(auth);

        auth = new Authorization();
        auth.setCode("SP");
        auth.setDescription("Spear");
        auth.setDate(new Date());
        auth.setOrderValue(new Long(++i));
        auths.add(auth);

        auth = new Authorization();
        auth.setCode("Marshal");
        auth.setDescription("Marshal");
        auth.setDate(new Date());
        auth.setOrderValue(new Long(++i));
        auths.add(auth);

        auth = new Authorization();
        auth.setCode("HR/CT");
        auth.setDescription("Heavy Rapier/Cut and Thrust");
        auth.setDate(new Date());
        auth.setOrderValue(new Long(++i));
        auths.add(auth);

        auth = new Authorization();
        auth.setCode("CA");
        auth.setDescription("Combat Archery");
        auth.setDate(new Date());
        auth.setOrderValue(new Long(++i));
        auths.add(auth);

        auth = new Authorization();
        auth.setCode("SE");
        auth.setDescription("Siege Engines");
        auth.setDate(new Date());
        auth.setOrderValue(new Long(++i));
        auths.add(auth);

        fighter.setAuthorization(auths);

        ScaGroup group = new ScaGroup();
        group.setGroupLocation("Central");
        group.setGroupName("Forgotten Sea");

        fighter.setScaGroup(group);

        List<Fighter> data = new ArrayList<>();
        data.add(fighter);

        instance.build(baosPDF, data, new DateTime(), new DateTime());
        try (FileOutputStream fos = new FileOutputStream("cardtest.pdf")) {
            baosPDF.writeTo(fos);
        }
    }
}
