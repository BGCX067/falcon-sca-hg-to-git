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
public class SearchEvent extends GwtEvent<SearchEventHandler> {

	public static Type<SearchEventHandler> TYPE = new GwtEvent.Type<SearchEventHandler>();

	public enum SearchType {

		FIGHTER, GROUP;
	}
	private String searchName = null;
	private ScaGroup group = null;
	private SearchType searchType = SearchType.FIGHTER;
    private boolean searchTypeChange = false;

	public SearchEvent() {
	}

	public SearchEvent(String searchName) {
		this.searchName = searchName;
        searchType = SearchType.FIGHTER;
	}

	public SearchEvent(ScaGroup group) {
		this.group = group;
        searchType = SearchType.GROUP;
	}

	public SearchEvent(SearchType searchType) {
		this.searchType = searchType;
        searchTypeChange = true;
	}

	@Override
	public Type<SearchEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SearchEventHandler handler) {
        if(searchTypeChange) {
			handler.switchSearchType(searchType);
        } else {
            if (searchType == SearchType.FIGHTER) {
                if (searchName == null && group == null) {
                    handler.loadAll();
                } else if (group == null) {
                    handler.find(searchName);
                }
            } else {
                handler.loadGroup(group);
            }
        }
	}
}
