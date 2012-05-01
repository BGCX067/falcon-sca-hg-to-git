/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.event.shared.EventHandler;
import org.sca.calontir.cmpe.dto.Fighter;

/**
 *
 * @author rikscarborough
 */
public interface DataUpdatedEventHandler extends EventHandler {

	public void fighterUpdated(Fighter fighter);
	public void fighterAdded();
	
}
