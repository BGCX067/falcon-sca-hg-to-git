package org.sca.calontir.cmpe.db;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import org.sca.calontir.cmpe.data.Fighter;

/**
 *
 * @author rik
 */
public class FighterDAO {

    public Fighter getFighter(int fighterId) {
        Fighter fighter = null;
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Key fighterKey = KeyFactory.createKey(Fighter.class.getSimpleName(), fighterId);
        fighter = (Fighter) pm.getObjectById(Fighter.class, fighterKey);

        return fighter;
    }

    public List<Fighter> getFighters() {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(Fighter.class);
        query.setOrdering("scaName");
        return (List<Fighter>) query.execute();
    }

    public void saveFighter(Fighter fighter) {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        try {
            fighter = pm.makePersistent(fighter);
        } finally {
            pm.close();
        }
    }
}
