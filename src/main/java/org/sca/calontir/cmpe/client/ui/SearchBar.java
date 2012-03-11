/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.*;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.*;
import java.util.LinkedList;
import java.util.List;
import org.sca.calontir.cmpe.client.FighterListInfo;

/**
 *
 * @author rikscarborough
 */
public class SearchBar extends Composite {

    private Storage stockStore = null;
    private Button submit;

    public SearchBar() {
        FlowPanel searchPanel = new FlowPanel();
        DOM.setElementAttribute(searchPanel.getElement(), "id", "searchBar");

        final SuggestBox box = buildSuggestBox();
        DOM.setElementAttribute(box.getElement(), "id", "search");
        DOM.setElementAttribute(box.getElement(), "autocomplete", "off");

        searchPanel.add(box);

        submit = new Button("Submit", new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                submit.setEnabled(false);
                DOM.getElementById("Signup-Form").getStyle().setDisplay(Style.Display.NONE);

                String searchName = box.getText();
                String foundName = null;
                stockStore = Storage.getLocalStorageIfSupported();
                List<FighterListInfo> fighterList = new LinkedList<FighterListInfo>();
                if (stockStore != null) {
                    String scaNameListStr = stockStore.getItem("scaNameList");
                    JSONValue value = JSONParser.parseStrict(scaNameListStr);
                    JSONObject scaNameObjs = value.isObject();
                    JSONArray scaNameArray = scaNameObjs.get("scaNames").isArray();

                    try {
                        for (int i = 0; i < scaNameArray.size() - 1; ++i) {
                            JSONObject scaNameObj = scaNameArray.get(i).isObject();
                            JSONString scaName = scaNameObj.get("scaName").isString();
                            JSONNumber id = scaNameObj.get("id").isNumber();
                            JSONString auths = scaNameObj.get("authorizations").isString();
                            JSONString group = scaNameObj.get("group").isString();
                            if (scaName.stringValue().toUpperCase().contains(searchName.toUpperCase())) {
                                FighterListInfo fli = new FighterListInfo();
                                fli.setFighterId(new Double(id.doubleValue()).longValue());
                                fli.setAuthorizations(auths.toString());
                                fli.setGroup(group.toString());
                                fighterList.add(fli);
                            }
                        }
                    } catch (Exception e) {
                        Window.alert(e.getMessage());
                    }

                }

                Window.alert(fighterList.toString());

                DOM.getElementById(
                        "List-Box").getStyle().setDisplay(Style.Display.INLINE_BLOCK);
                submit.setEnabled(
                        true);
            }
        });

        searchPanel.add(submit);

        initWidget(searchPanel);
    }

    private SuggestBox buildSuggestBox() {
        final MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
        stockStore = Storage.getLocalStorageIfSupported();
        if (stockStore != null) {
            String scaNameListStr = stockStore.getItem("scaNameList");
            JSONValue value = JSONParser.parseStrict(scaNameListStr);
            JSONObject scaNameObjs = value.isObject();
            JSONArray scaNameArray = scaNameObjs.get("scaNames").isArray();

            for (int i = 0; i < scaNameArray.size() - 1; ++i) {
                JSONObject scaNameObj = scaNameArray.get(i).isObject();
                JSONString scaName = scaNameObj.get("scaName").isString();

                oracle.add(scaName.stringValue());
            }
        }


        return new SuggestBox(oracle);
    }
}
