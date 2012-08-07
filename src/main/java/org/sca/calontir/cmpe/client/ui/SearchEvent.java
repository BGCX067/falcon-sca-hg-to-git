/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.event.shared.GwtEvent;
import org.sca.calontir.cmpe.dto.ScaGroup;

/**
 *
 * @author rikscarborough
 */
public class SearchEvent extends GwtEvent<SearchEventHandler>{

	public static Type<SearchEventHandler> TYPE = new GwtEvent.Type<SearchEventHandler>();
	private String searchName = null;
	private ScaGroup group = null;

	public SearchEvent() {
		
	}

	public SearchEvent(String searchName) {
		this.searchName = searchName;
	}

	public SearchEvent(ScaGroup group) {
		this.group = group;
	}

	@Override
	public Type<SearchEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SearchEventHandler handler) {
		if(searchName == null && group == null) {
			handler.loadAll();
		} else if (group == null) {
			handler.find(searchName);
		} else {

		}

	}
	
}
