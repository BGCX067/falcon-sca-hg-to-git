package org.sca.calontir.cmpe.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.storage.client.StorageMap;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import java.util.List;
import org.sca.calontir.cmpe.client.ui.CalonBar;
import org.sca.calontir.cmpe.client.ui.SearchBar;

public class IndexPage implements EntryPoint {

    private Storage stockStore = null;

    /**
     * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
     */
    @Override
    public void onModuleLoad() {
        loadData();
    }

    private void buildIndexPage() {
        // remove Loading-Message from page
        RootPanel.getBodyElement().removeChild(
                DOM.getElementById("Loading-Message"));


        CalonBar calonBar = new CalonBar();
        RootPanel.get().add(calonBar);

        SearchBar searchBar = new SearchBar();
        RootPanel.get().add(searchBar);

    }

    private void loadData() {

        stockStore = Storage.getLocalStorageIfSupported();
        if (stockStore != null) {
            StorageMap stockMap = new StorageMap(stockStore);
            if (stockMap.containsValue("scaNameList") != true) {
                FighterServiceAsync fighterService = GWT.create(FighterService.class);
                fighterService.getListItems(new AsyncCallback<List<FighterListInfo>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("Failed lookup " + caught.getMessage());
                        buildIndexPage();
                    }

                    @Override
                    public void onSuccess(List<FighterListInfo> result) {
                        StringBuilder scaNameListStr = new StringBuilder();
                        boolean first = true;
                        for (FighterListInfo fli : result) {
                            if (first) {
                                first = false;
                            } else {
                                scaNameListStr.append(";");
                            }
                            scaNameListStr.append(fli.getScaName());
                        }
                        stockStore.setItem("scaNameList", scaNameListStr.toString());
                        buildIndexPage();
                    }
                });
            }
        }
    }
}
