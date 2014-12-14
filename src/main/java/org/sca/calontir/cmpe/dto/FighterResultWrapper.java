/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.dto;

import com.google.appengine.api.datastore.Cursor;
import java.util.List;

/**
 *
 * @author rikscarborough
 */
public class FighterResultWrapper {

    private List<FighterListItem> fighters;
    private Cursor cursor;

    public List<FighterListItem> getFighters() {
        return fighters;
    }

    public void setFighters(List<FighterListItem> fighters) {
        this.fighters = fighters;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

}
