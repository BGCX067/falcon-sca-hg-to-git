/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.sca.calontir.cmpe.db;

import java.util.*;

/**
 *
 * @author rik
 */
public abstract class LocalCacheAbImpl implements LocalCache {
    
    private Map<Object, Object> data = new HashMap<Object, Object>();

    public Object getValue(Object key) {
        return data.get(key);
    }

    public void put(Object key, Object value) {
        data.put(key, value);
    }
    
    public List getValueList() {
        List dataList = new ArrayList(data.values());
        Collections.sort(dataList);
        return dataList;
    }

    @Override
    public void clear() {
        data.clear();
    }

    @Override
    public int getCount() {
        return data.size();
    }
    
}
