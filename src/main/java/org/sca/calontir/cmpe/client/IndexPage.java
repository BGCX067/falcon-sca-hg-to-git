package org.sca.calontir.cmpe.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.storage.client.StorageMap;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
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
        RootPanel.getBodyElement().removeChild(DOM.getElementById("Loading-Message"));


        CalonBar calonBar = new CalonBar();
        RootPanel.get().add(calonBar);

        SearchBar searchBar = new SearchBar();
        RootPanel.get().add(searchBar);


        // if index page
        onIndexPage();
        foundMultibleResults();

    }

    private void onIndexPage() {
        Panel signupPanel = new FlowPanel();
        signupPanel.setStyleName("dataBox");
        signupPanel.getElement().setId("Signup-Form");

        Panel innerSignupPanel = new FlowPanel();
        innerSignupPanel.setStyleName("dataBody");

        signupPanel.add(innerSignupPanel);

        Label p = new Label();
        p.setText("Not registered? Sign up now!");

        Label p2 = new Label();
        p2.setText("Registering with the Calontir Marshalallate system allows you review your own authorizations, "
                + "update your contact information, and print your own fighter card at home.");

        HTML form = new HTML("<iframe src=\"https://docs.google.com/spreadsheet/embeddedform?formkey=dGNDV2NYdGUtZk1aZXN6MURkaWlFNlE6MQ\" "
                + "width=\"700\" height=\"853\" frameborder=\"0\" marginheight=\"0\" marginwidth=\0\">Loading Signup Form...</iframe>");

        innerSignupPanel.add(p);
        innerSignupPanel.add(p2);
        innerSignupPanel.add(form);

        RootPanel.get().add(signupPanel);

    }
    
    private void foundMultibleResults() {
        Panel listPanel = new FlowPanel();
        listPanel.setStyleName("list");
        listPanel.getElement().setId("List-Box");

        Panel listHeader = new FlowPanel();
        listHeader.setStyleName("listRow");
        listHeader.addStyleName("header");

        listPanel.add(listHeader);
        listPanel.getElement().getStyle().setDisplay(Style.Display.NONE);
        
        Label scaNameHeader = new Label();
        scaNameHeader.setText("SCA Name");
        
        RootPanel.get().add(listPanel);
        
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
