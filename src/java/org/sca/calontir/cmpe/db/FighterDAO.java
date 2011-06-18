package org.sca.calontir.cmpe.db;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import java.util.ArrayList;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import org.sca.calontir.cmpe.data.Fighter;
import org.sca.calontir.cmpe.dto.DataTransfer;

/**
 *
 * @author rik
 */
public class FighterDAO {

    public org.sca.calontir.cmpe.dto.Fighter getFighter(long fighterId) {
        Fighter fighter = null;
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Key fighterKey = KeyFactory.createKey(Fighter.class.getSimpleName(), fighterId);
        fighter = (Fighter) pm.getObjectById(Fighter.class, fighterKey);

        return DataTransfer.convert(fighter);
    }

    public Fighter getFighterDO(long fighterId) {
        Fighter fighter = null;
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Key fighterKey = KeyFactory.createKey(Fighter.class.getSimpleName(), fighterId);
        fighter = (Fighter) pm.getObjectById(Fighter.class, fighterKey);

        return fighter;
    }

    public List<org.sca.calontir.cmpe.dto.Fighter> queryFightersByScaName(String scaName) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
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
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(Fighter.class);
        query.setOrdering("scaName");
        List<Fighter> fighters = (List<Fighter>) query.execute();
        List<org.sca.calontir.cmpe.dto.Fighter> retArray = new ArrayList<org.sca.calontir.cmpe.dto.Fighter>();
        for (Fighter f : fighters) {
            retArray.add(DataTransfer.convert(f));
        }
        return retArray;
    }

    public org.sca.calontir.cmpe.dto.Fighter saveFighter(org.sca.calontir.cmpe.dto.Fighter fighter) {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        Fighter f = null;
        if (fighter.getFighterId() != null && fighter.getFighterId() > 0) {
            Key fighterKey = KeyFactory.createKey(Fighter.class.getSimpleName(), fighter.getFighterId());
            f = (Fighter) pm.getObjectById(Fighter.class, fighterKey);
        }
        f = DataTransfer.convert(fighter, f);
        try {
            f = pm.makePersistent(f);
            pm.flush();
        } finally {
            pm.close();
        }
        return DataTransfer.convert(f);
    }
}
