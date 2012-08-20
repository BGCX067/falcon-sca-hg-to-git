package org.sca.calontir.cmpe.db;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.sca.calontir.cmpe.ValidationException;
import org.sca.calontir.cmpe.data.Address;
import org.sca.calontir.cmpe.data.Fighter;
import org.sca.calontir.cmpe.data.Note;
import org.sca.calontir.cmpe.data.TableUpdates;
import org.sca.calontir.cmpe.dto.DataTransfer;
import org.sca.calontir.cmpe.dto.FighterListItem;

/**
 *
 * @author rik
 */
public class FighterDAO {

    private final PersistenceManager pm = PMF.get().getPersistenceManager();
    private FighterCache fCache = FighterCache.getInstance();

    public FighterDAO() {
    }

    public org.sca.calontir.cmpe.dto.Fighter getFighter(long fighterId) {
        org.sca.calontir.cmpe.dto.Fighter retVal = fCache.getFighter(fighterId);
        if (retVal == null) {
            Logger.getLogger(getClass().getName()).log(Level.INFO, String.format("Getting %d from datastore", fighterId));
            Fighter fighter = getFighterDO(fighterId);
            retVal = DataTransfer.convert(fighter);
            fCache.put(retVal);
        }

        return retVal;
    }

    public Fighter getFighterDO(long fighterId) {
        Key fighterKey = KeyFactory.createKey(Fighter.class.getSimpleName(), fighterId);
        Fighter fighter = (Fighter) pm.getObjectById(Fighter.class, fighterKey);
        return fighter;
    }

    public org.sca.calontir.cmpe.dto.Fighter getFighterByGoogleId(String userId) {
        org.sca.calontir.cmpe.dto.Fighter retval = fCache.getFighterByGoogleId(userId);
        if (retval == null) {
            Query query = pm.newQuery(Fighter.class);
            query.setFilter("googleId == googleIdParam");
            query.declareParameters("String googleIdParam");
            List<Fighter> fighters = (List<Fighter>) query.execute(userId);

            if (fighters != null && fighters.size() > 0) {
                retval = DataTransfer.convert(fighters.get(0));
            } else {
				retval = getByGoogleIdLowerCase(userId);
			}
        }
        return retval;
    }

	//TODO: if it returns a value, the record should be updated to reflect the correct address.
    private org.sca.calontir.cmpe.dto.Fighter getByGoogleIdLowerCase(final String userId) {
            final Query query = pm.newQuery(Fighter.class);
            query.setFilter("googleId == googleIdParam");
            query.declareParameters("String googleIdParam");
            final List<Fighter> fighters = (List<Fighter>) query.execute(userId.toLowerCase());

            if (fighters != null && fighters.size() > 0) {
                return DataTransfer.convert(fighters.get(0));
            }
			return null;
    }
	

    public List<org.sca.calontir.cmpe.dto.Fighter> queryFightersByScaName(String scaName) {
        List<org.sca.calontir.cmpe.dto.Fighter> retArray = new ArrayList<org.sca.calontir.cmpe.dto.Fighter>();
        if (StringUtils.isBlank(scaName)) {
            return retArray;
        }
        Query query = pm.newQuery(Fighter.class);
        query.setFilter("scaName == scaNameParam");
        query.declareParameters("String scaNameParam");
        List<Fighter> fighters = (List<Fighter>) query.execute(scaName);
        if (fighters == null || fighters.isEmpty()) {
            List<Fighter> allFighters = returnAllFighters();
            fighters = filterByScaName(allFighters, scaName);
        }
        for (Fighter f : fighters) {
            retArray.add(DataTransfer.convert(f));
        }
        return retArray;
    }

    public List<FighterListItem> getFighterListByScaName(String scaName) {
        List<FighterListItem> retArray = new ArrayList<FighterListItem>();
        List<FighterListItem> fighters = getFighterListItems();
        if (StringUtils.isBlank(scaName)) {
            return fighters;
        }
        for (FighterListItem f : fighters) {
            if (f.getScaName() != null
                    && f.getScaName().toUpperCase().contains(scaName.toUpperCase())) {
                retArray.add(f);
            }
        }
        return retArray;
    }

    private List<Fighter> filterByScaName(List<Fighter> allFighters, String scaName) {
        List<Fighter> retList = new ArrayList<Fighter>();
        if (scaName == null || StringUtils.isBlank(scaName)) {
            return retList;
        }

        for (Fighter f : allFighters) {
            if (f.getScaName() != null) {
                if (f.getScaName().toUpperCase().contains(scaName.toUpperCase())) {
                    retList.add(f);
                }
            }
        }
        return retList;
    }

    public List<FighterListItem> getFighterListItems(DateTime dt) {
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Getting fighter list from datastore as of {0}", dt);
        if (dt == null || dt.getYear() == 1966) {
            return getFighterListItems();
        }

        List<Fighter> fighters = getAllFightersAsOf(dt);
        Map<Long, FighterListItem> fighterListMap = new HashMap<Long, FighterListItem>();
        for (Fighter f : fighters) {
            FighterListItem fli = DataTransfer.convertToListItem(f);
            fighterListMap.put(fli.getFighterId(), fli);
        }
        List<FighterListItem> retVal = new ArrayList<FighterListItem>(fighterListMap.values());
        Collections.sort(retVal);
        return retVal;
    }

    public List<FighterListItem> getFighterListItems() {
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Getting fighter list from datastore as of {0}", fCache.getLastUpdate());
        List<Fighter> fighters = getAllFightersAsOf(fCache.getLastUpdate());
        Map<Long, FighterListItem> fighterListMap = new HashMap<Long, FighterListItem>();
        for (Fighter f : fighters) {
            FighterListItem fli = DataTransfer.convertToListItem(f);
            fighterListMap.put(fli.getFighterId(), fli);
        }
        fCache.putAll(fighterListMap);
        return fCache.getFighterList();
    }

    private List<Fighter> getAllFightersAsOf(DateTime dt) {
        if (dt == null || dt.toDateMidnight().equals(new DateMidnight(1966, 3, 1))) {
            return returnAllFighters();
        }
        Query query = pm.newQuery(Fighter.class);
        query.setFilter("lastUpdated > lastUpdateParam");
        query.declareImports("import java.util.Date");
        query.declareParameters("Date lastUpdateParam");
        List<Fighter> fighters = (List<Fighter>) query.execute(dt.toDate());

        Logger.getLogger(getClass().getName()).log(Level.INFO,
                "Getting updated fighters: {0} as of {1}", new Object[]{fighters.size(), dt.toString()});
        return fighters;
    }

    private List<Fighter> returnAllFighters() {
        Query query = pm.newQuery(Fighter.class);
        query.setOrdering("scaName");
        List<Fighter> fighters = (List<Fighter>) query.execute();
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Getting all fighters: {0}", fighters.size());
        return fighters;
    }

    public List<org.sca.calontir.cmpe.dto.Fighter> getFighters() {
        List<Fighter> fighters = returnAllFighters();
        List<org.sca.calontir.cmpe.dto.Fighter> retArray = new ArrayList<org.sca.calontir.cmpe.dto.Fighter>();
        for (Fighter f : fighters) {
            retArray.add(DataTransfer.convert(f));
        }
        return retArray;
    }

    public Long saveFighter(org.sca.calontir.cmpe.dto.Fighter fighter, Long userId) throws ValidationException {
        return this.saveFighter(fighter, userId, true);
    }

    public Long saveFighter(org.sca.calontir.cmpe.dto.Fighter fighter, Long userId, boolean validate) throws ValidationException {
        Long keyValue = null;

        Fighter f = null;

        if (fighter.getFighterId() != null && fighter.getFighterId() > 0) {
            Key fighterKey = KeyFactory.createKey(Fighter.class.getSimpleName(), fighter.getFighterId());
            f = (Fighter) pm.getObjectById(Fighter.class, fighterKey);
            fCache.remove(fighter.getFighterId());
        }
        f = DataTransfer.convert(fighter, f);
        if (validate) {
            validate(f);
        }
        try {
			if(f.getNote() != null && fighter.getNote() == null) {
				Note note = f.getNote();
				pm.deletePersistent(note);
				f.setNote(note);
			}
            f.setLastUpdated(new Date());
            f.setUserUpdated(userId);
            Logger.getLogger(getClass().getName()).log(Level.INFO, "Saving {0}", f.getScaName());
            f = pm.makePersistent(f);

            Query query = pm.newQuery(TableUpdates.class);
            query.setFilter("tableName == tableNameParam");
            query.declareParameters("String tableNameParam");
            List<TableUpdates> tableUpdates = (List<TableUpdates>) query.execute("Fighter");

            TableUpdates tu = tableUpdates.size() > 0 ? tableUpdates.get(0) : null;
            if (tu == null) {
                tu = new TableUpdates();
                tu.setTableName("Fighter");
                tu.setLastUpdated(new Date());
            }
            pm.makePersistent(tu);
            pm.flush();
            if (f.getFighterId() != null) {
                keyValue = f.getFighterId().getId();
            }

        } finally {
            pm.close();
        }
        return keyValue;
    }

    public void deleteFighter(Long fighterId) {
        fCache.remove(fighterId);
        Key fighterKey = KeyFactory.createKey(Fighter.class.getSimpleName(), fighterId);
        Fighter f = (Fighter) pm.getObjectById(Fighter.class, fighterKey);
        try {
            pm.deletePersistent(f);
            Query query = pm.newQuery(TableUpdates.class);
            query.setFilter("tableName == tableNameParam");
            query.declareParameters("String tableNameParam");
            List<TableUpdates> tableUpdates = (List<TableUpdates>) query.execute("Fighter");

            TableUpdates tu = tableUpdates.size() > 0 ? tableUpdates.get(0) : null;
            if (tu == null) {
                tu = new TableUpdates();
                tu.setTableName("Fighter");
                tu.setLastDeletion(new Date());
            }
            pm.makePersistent(tu);
        } finally {
            pm.close();
        }
    }

//    public void deleteAuthorization(Long fighterId, Authorization authorization) {
//        fCache.remove(fighterId);
//        authorization = (Authorization) pm.getObjectById(authorization.getAuthorizatoinId());
//        pm.deletePersistent(authorization);
//    }
    protected boolean validate(Fighter fighter) throws ValidationException {
        if (fighter.getScaGroup() == null) {
            throw new ValidationException("Please select SCA Group");
        }

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
            query = pm.newQuery(Fighter.class);
            query.setFilter("modernName == modernNameParam");
            query.declareParameters("String modernNameParam");
            fighters = (List<Fighter>) query.execute(fighter.getModernName());
            if (fighters != null && fighters.size() > 0) {
                for (Fighter f : fighters) {
                    if (f.getAddress() != null && f.getAddress().size() > 0) {
                        Address a = f.getAddress().get(0);
                        if (StringUtils.equals(a.getAddress1(), address.getAddress1())
                                && StringUtils.equals(a.getCity(), address.getCity())
                                && StringUtils.equals(a.getState(), address.getState())) {
                            throw new ValidationException("A fighter with that modern name and address already exists in the database");
                        }
                    }
                }
            }
        }
        return true;
    }
}
