package org.sca.calontir.cmpe.db;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import java.util.ArrayList;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import org.apache.commons.lang.StringUtils;
import org.sca.calontir.cmpe.ValidationException;
import org.sca.calontir.cmpe.data.Address;
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
        if (StringUtils.isNotBlank(fighter.getScaMemberNo())) {
            query = pm.newQuery(Fighter.class);
            query.setFilter("scaMemberNo == scaMemberNoParam");
            query.declareParameters("String scaMemberNoParam");
            fighters = (List<Fighter>) query.execute(fighter.getScaMemberNo());
            if (fighters != null && fighters.size() > 0) {
                throw new ValidationException("A fighter with member no " + fighter.getScaMemberNo() + " already exists in the database");
            }
        }
        if (StringUtils.isNotBlank(fighter.getGoogleId())) {
            query = pm.newQuery(Fighter.class);
            query.setFilter("googleId == googleIdParam");
            query.declareParameters("String googleIdParam");
            fighters = (List<Fighter>) query.execute(fighter.getGoogleId());
            if (fighters != null && fighters.size() > 0) {
                throw new ValidationException("A fighter with Google Id " + fighter.getGoogleId() + " already exists in the database");
            }
        }
        if (fighter.getAddress() != null && fighter.getAddress().size() > 0) {
            Address address = fighter.getAddress().get(0);
            query = pm.newQuery("select from " + Fighter.class.getName()
                    + " where modernName == '" + fighter.getModernName() + "'");
            fighters = (List<Fighter>) query.execute();
            if (fighters != null && fighters.size() > 0) {
                for (Fighter f : fighters) {
                    Address a = f.getAddress().get(0);
                    if (StringUtils.equals(a.getAddress1(), address.getAddress1())
                            && StringUtils.equals(a.getCity(), address.getCity())
                            && StringUtils.equals(a.getState(), address.getState())) {
                        throw new ValidationException("A fighter with that modern name and address already exists in the database");
                    }
                }
            }
        }
        return true;
    }
}
