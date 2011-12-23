/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.sca.calontir.cmpe.db;

import java.util.HashMap;
import java.util.Map;

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

    @Override
    public void clear() {
        data.clear();
    }

    @Override
    public int getCount() {
        return data.size();
    }
    
}
