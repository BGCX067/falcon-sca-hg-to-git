package org.sca.calontir.cmpe.db;

import java.util.*;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.sca.calontir.cmpe.dto.Fighter;
import org.sca.calontir.cmpe.dto.FighterListItem;

/**
 *
 * @author rik
 */
public class FighterCache implements LocalCache {

    final private static FighterCache _instance = new FighterCache();
    private Map<Long, Fighter> _fighterMap = new HashMap<Long, Fighter>();
    private Map<String, Long> _fighterByGoogleId = new HashMap<String, Long>();
    private Map<Long, FighterListItem> _fighterListMap = new HashMap<Long, FighterListItem>();
    private static DateTime lastUpdated = null;

    private FighterCache() {
    }

    public static FighterCache getInstance() {
        return _instance;
    }

    public DateTime getLastUpdate() {
        return lastUpdated;
    }

    private synchronized void _updated() {
        DateTime now = new DateTime();
        if (lastUpdated == null || now.isAfter(lastUpdated)) {
            lastUpdated = now;
        }
    }

    public Fighter getFighter(Long id) {
        return _fighterMap.get(id);
    }

    public Fighter getFighterByGoogleId(String googleId) {
        Long key = _fighterByGoogleId.get(googleId);
        if (key == null) {
            return null;
        }
        return getFighter(key);
    }

    public FighterListItem getFighterListItem(Long id) {
        return _fighterListMap.get(id);
    }

    public List<FighterListItem> getFighterList() {
        List<FighterListItem> retVal = new ArrayList<FighterListItem>(_fighterListMap.values());
        Collections.sort(retVal);
        return retVal;
    }

    public void putAll(Map<Long, FighterListItem> fighterMap) {
        _updated();
        _fighterListMap.putAll(fighterMap);
    }

    public void put(Fighter fighter) {
        _updated();
        _fighterMap.put(fighter.getFighterId(), fighter);
        if (!StringUtils.isEmpty(fighter.getGoogleId())) {
            _fighterByGoogleId.put(fighter.getGoogleId(), fighter.getFighterId());
        }
    }

    public void put(FighterListItem fighter) {
        _updated();
        _fighterListMap.put(fighter.getFighterId(), fighter);
    }

    public void remove(Long id) {
        removeFighter(id);
        removeFighterListItem(id);
    }

    public void removeFighter(Long id) {
        _fighterMap.remove(id);
    }

    public void removeFighterListItem(Long id) {
        _fighterListMap.remove(id);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}