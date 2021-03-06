/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.event.shared.EventHandler;
import org.sca.calontir.cmpe.dto.ScaGroup;

/**
 *
 * @author rikscarborough
 */
public interface SearchEventHandler extends EventHandler {

	public void find(String searchName);

	public void loadAll();

	public void loadGroup(ScaGroup group);

	public void switchSearchType(SearchEvent.SearchType searchType);

}
