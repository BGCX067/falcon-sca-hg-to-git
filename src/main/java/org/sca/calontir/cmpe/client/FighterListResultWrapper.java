package org.sca.calontir.cmpe.client;

import java.io.Serializable;

/**
 *
 * @author rikscarborough
 */
public class FighterListResultWrapper implements Serializable {

    private FighterListInfo fighters;
    private String cursor;
    private Integer count;
    private Integer pageSize;

    public FighterListInfo getFighters() {
        return fighters;
    }

    public void setFighters(FighterListInfo fighters) {
        this.fighters = fighters;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

}
