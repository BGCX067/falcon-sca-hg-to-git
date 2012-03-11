package org.sca.calontir.cmpe.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.storage.client.StorageMap;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import java.util.LinkedList;
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
                + "width=\"620\" height=\"820\" frameborder=\"0\" marginheight=\"0\" marginwidth=\0\">Loading Signup Form...</iframe>");

        innerSignupPanel.add(p);
        innerSignupPanel.add(p2);
        innerSignupPanel.add(form);

        RootPanel.get().add(signupPanel);

    }
    
    private void foundMultibleResults() {
        Panel listPanel = new FlowPanel();
        listPanel.setStyleName("list");
        listPanel.getElement().setId("List-Box");

//        final CellTable<Contact> table = new CellTable<Contact>();
        
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
                        JSONObject scaNameList = new JSONObject();
                        boolean first = true;
                        JSONArray scaNameObjs = new JSONArray();
                        int i = 0;
                        for (FighterListInfo fli : result) {
                            JSONString scaName = new JSONString(fli.getScaName());
                            JSONNumber id = new JSONNumber(fli.getFighterId());
                            JSONString auths = new JSONString(fli.getAuthorizations());
                            JSONString group = new JSONString(fli.getGroup());
                            JSONObject scaNameObj = new JSONObject();
                            scaNameObj.put("scaName", scaName);
                            scaNameObj.put("id", id);
                            scaNameObj.put("authorizations", auths);
                            scaNameObj.put("group", group);
                            scaNameObjs.set(i++, scaNameObj);
                        }
                        scaNameList.put("scaNames", scaNameObjs);
                        stockStore.setItem("scaNameList", scaNameList.toString());
                        buildIndexPage();
                    }
                });
            }
        }
    }
}
