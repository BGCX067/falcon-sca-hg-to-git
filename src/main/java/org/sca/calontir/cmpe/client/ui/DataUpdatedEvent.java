/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.event.shared.GwtEvent;
import org.sca.calontir.cmpe.dto.Fighter;

/**
 *
 * @author rikscarborough
 */
public class DataUpdatedEvent extends GwtEvent<DataUpdatedEventHandler> {

	public static Type<DataUpdatedEventHandler> TYPE = new GwtEvent.Type<DataUpdatedEventHandler>();
	private Fighter fighter;

	public DataUpdatedEvent() {

	}

	public DataUpdatedEvent(Fighter fighter) {
		this.fighter = fighter;
	}

	@Override
	public Type<DataUpdatedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(DataUpdatedEventHandler handler) {
		if (fighter != null && fighter.getFighterId() != null && fighter.getFighterId() > 0) {
			handler.fighterUpdated(fighter);
		} else {
			handler.fighterAdded();
		}
	}
}
