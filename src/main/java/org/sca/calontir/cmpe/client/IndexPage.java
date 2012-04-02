package org.sca.calontir.cmpe.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.*;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import java.util.Date;
import org.sca.calontir.cmpe.client.ui.*;
import org.sca.calontir.cmpe.client.user.Security;
import org.sca.calontir.cmpe.client.user.SecurityFactory;

public class IndexPage implements EntryPoint {

    private Storage stockStore = null;
    final private ListDataProvider<FighterInfo> dataProvider = new ListDataProvider<FighterInfo>();
    final private CellTable<FighterInfo> table = new CellTable<FighterInfo>();
    final private Security security = SecurityFactory.getSecurity();
    private FighterIdBoxEdit fighterId = new FighterIdBoxEdit();

    /**
     * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
     */
    @Override
    public void onModuleLoad() {
//        security = GWT.create(SecurityService.class);
        loadData();
    }

    private void buildIndexPage() {
        // remove Loading-Message from page
        RootPanel.getBodyElement().removeChild(DOM.getElementById("Loading-Message"));


        CalonBar calonBar = new CalonBar();
        RootPanel.get().add(calonBar);

        SearchBar searchBar = new SearchBar(table, dataProvider);
        searchBar.addHandler(fighterId, EditViewEvent.TYPE);
        RootPanel.get().add(searchBar);


        onIndexPage();
        foundMultibleResults();
        buildFighterForm();

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
        //TODO: move table and dataProvider to flb and use an event to kick this off from SearchPanel
         FighterListBox flb = new FighterListBox(table, dataProvider);
         flb.addHandler(fighterId, EditViewEvent.TYPE);
         RootPanel.get().add(flb);
    }

    private void buildFighterForm() {
        final FormPanel fighterForm = new FormPanel();
        fighterForm.setAction("/fighterg");
        fighterForm.getElement().setId("FighterForm");
        fighterForm.getElement().getStyle().setDisplay(Style.Display.NONE);
        fighterForm.setMethod(FormPanel.METHOD_POST);
        
        fighterForm.add(fighterId);
        
        fighterForm.addSubmitHandler(new FormPanel.SubmitHandler() {

            @Override
            public void onSubmit(FormPanel.SubmitEvent event) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        
        fighterForm.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {

            @Override
            public void onSubmitComplete(FormPanel.SubmitCompleteEvent event) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        
        RootPanel.get().add(fighterForm);
    }


    private void loadData() {

        stockStore = Storage.getLocalStorageIfSupported();
        if (stockStore != null) {
            FighterServiceAsync fighterService = GWT.create(FighterService.class);



            final String scaNameListStr = stockStore.getItem("scaNameList");
            String timeStampStr = stockStore.getItem("scaNameUpdated");
            Date targetDate = null;
            if (timeStampStr == null || timeStampStr.trim().isEmpty()) {
                targetDate = null;
            } else {
                long timeStamp = Long.valueOf(timeStampStr);
                targetDate = new Date(timeStamp);
            }
            //TODO: Change to get a single object back that contains the list and'
            // a flag to represent if the data should be updated or replaced.
            fighterService.getListItems(targetDate, new AsyncCallback<FighterListInfo>() {

                @Override
                public void onFailure(Throwable caught) {
                    Window.alert("Failed lookup " + caught.getMessage());
                    buildIndexPage();
                }

                @Override
                public void onSuccess(FighterListInfo result) {
                    JSONArray scaNameObjs;
                    if (scaNameListStr == null || !result.isUpdateInfo()) {
                        scaNameObjs = new JSONArray();
                    } else {
                        JSONValue value = JSONParser.parseStrict(scaNameListStr);
                        JSONObject scaNameObj = value.isObject();
                        scaNameObjs = scaNameObj.get("scaNames").isArray();
                    }
                    JSONObject scaNameList = new JSONObject();

                    int i = scaNameObjs.size();
                    if (result.getFighterInfo() != null && result.getFighterInfo().size() > 0) {
                        for (FighterInfo fli : result.getFighterInfo()) {
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
                        stockStore.removeItem("scaNameList");
                        stockStore.setItem("scaNameList", scaNameList.toString());
                        Date now = new Date();
                        stockStore.setItem("scaNameUpdated", Long.toString(now.getTime()));
                    }
                    buildIndexPage();
                }
            });

        }
    }
}
