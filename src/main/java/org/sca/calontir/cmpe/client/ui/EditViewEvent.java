/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.Window;
import org.sca.calontir.cmpe.client.FighterInfo;

/**
 *
 * @author rikscarborough
 */
public class EditViewEvent extends GwtEvent<EditViewHandler> {

    public static Type<EditViewHandler> TYPE = new GwtEvent.Type<EditViewHandler>();
    private Mode mode;
    private FighterInfo fighter;

    public EditViewEvent(Mode mode) {
        this.mode = mode;
    }

    public EditViewEvent(Mode mode, FighterInfo fighter) {
        this(mode);
        this.fighter = fighter;
    }

    @Override
    public Type<EditViewHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(EditViewHandler handler) {
        switch (mode) {
            case ADD:
                handler.buildAdd();
                break;

            case VIEW:
                ((FighterIdBoxEdit)handler).setFighter(fighter);
                handler.buildView();
                break;

            default:
                throw new UnsupportedOperationException("Not supported yet.");
        }


    }
}
