package org.sca.calontir.cmpe.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;
import org.sca.calontir.cmpe.client.ui.CalonBar;
import org.sca.calontir.cmpe.client.ui.SearchBar;

public class IndexPage implements EntryPoint {

    /**
     * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
     */
    @Override
    public void onModuleLoad() {

        // remove Loading-Message from page
        RootPanel.getBodyElement().removeChild(
                DOM.getElementById("Loading-Message"));


        CalonBar calonBar = new CalonBar();
        
        RootPanel.get().add(calonBar);
        
        SearchBar searchBar = new SearchBar();
        RootPanel.get().add(searchBar);
    }
}
