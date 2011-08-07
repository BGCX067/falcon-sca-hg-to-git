package org.sca.calontir.cmpe.db;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import java.util.ArrayList;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import org.sca.calontir.cmpe.ValidationException;
import org.sca.calontir.cmpe.data.Fighter;
import org.sca.calontir.cmpe.dto.DataTransfer;

/**
 *
 * @author rik
 */
public class FighterDAO {

    private final PersistenceManager pm = PMF.get().getPersistenceManager();

    public FighterDAO() {
    }

    public org.sca.calontir.cmpe.dto.Fighter getFighter(long fighterId) {
        Fighter fighter = null;
        Key fighterKey = KeyFactory.createKey(Fighter.class.getSimpleName(), fighterId);
        fighter = (Fighter) pm.getObjectById(Fighter.class, fighterKey);

        return DataTransfer.convert(fighter);
    }

    public Fighter getFighterDO(long fighterId) {
        Fighter fighter = null;
        Key fighterKey = KeyFactory.createKey(Fighter.class.getSimpleName(), fighterId);
        fighter = (Fighter) pm.getObjectById(Fighter.class, fighterKey);

        return fighter;
    }

    public org.sca.calontir.cmpe.dto.Fighter getFighterByGoogleId(String userId) {
        Query query = pm.newQuery(Fighter.class);
        query.setFilter("googleId == googleIdParam");
        query.declareParameters("String googleIdParam");
        List<Fighter> fighters = (List<Fighter>) query.execute(userId);
        org.sca.calontir.cmpe.dto.Fighter retval = null;
        if (fighters != null && fighters.size() > 0) {
            retval = DataTransfer.convert(fighters.get(0));
        }
        return retval;
    }

    public List<org.sca.calontir.cmpe.dto.Fighter> queryFightersByScaName(String scaName) {
        Query query = pm.newQuery(Fighter.class);
        query.setFilter("scaName == scaNameParam");
        query.declareParameters("String scaNameParam");
        List<Fighter> fighters = (List<Fighter>) query.execute(scaName);
        List<org.sca.calontir.cmpe.dto.Fighter> retArray = new ArrayList<org.sca.calontir.cmpe.dto.Fighter>();
        for (Fighter f : fighters) {
            retArray.add(DataTransfer.convert(f));
        }
        return retArray;
    }

    public List<org.sca.calontir.cmpe.dto.Fighter> getFighters() {
        Query query = pm.newQuery(Fighter.class);
        query.setOrdering("scaName");
        List<Fighter> fighters = (List<Fighter>) query.execute();
        List<org.sca.calontir.cmpe.dto.Fighter> retArray = new ArrayList<org.sca.calontir.cmpe.dto.Fighter>();
        for (Fighter f : fighters) {
            retArray.add(DataTransfer.convert(f));
        }
        return retArray;
    }

    public Long saveFighter(org.sca.calontir.cmpe.dto.Fighter fighter) throws ValidationException {
        return this.saveFighter(fighter, true);
    }

    public Long saveFighter(org.sca.calontir.cmpe.dto.Fighter fighter, boolean validate) throws ValidationException {
        Long keyValue = null;

        Fighter f = null;
        if (fighter.getFighterId() != null && fighter.getFighterId() > 0) {
            Key fighterKey = KeyFactory.createKey(Fighter.class.getSimpleName(), fighter.getFighterId());
            f = (Fighter) pm.getObjectById(Fighter.class, fighterKey);
        }
        f = DataTransfer.convert(fighter, f);
        if (validate) {
            validate(f);
        }
        try {
            f = pm.makePersistent(f);
            pm.flush();
            if (f.getFighterId() == null) {
                System.out.println("Key not updated.");
            } else {
                keyValue = f.getFighterId().getId();
            }
        } finally {
            pm.close();
        }
        return keyValue;
    }

    protected boolean validate(Fighter fighter) throws ValidationException {
        Query query = pm.newQuery(Fighter.class);
        query.setFilter("scaName == scaNameParam");
        query.declareParameters("String scaNameParam");
        List<Fighter> fighters = (List<Fighter>) query.execute(fighter.getScaName());
        if (fighters != null && fighters.size() > 0) {
            throw new ValidationException(fighter.getScaName() + " already exists in the database");
        }
        return true;
    }
}
