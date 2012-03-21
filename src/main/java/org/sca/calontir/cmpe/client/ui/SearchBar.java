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
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.ListDataProvider;
import java.util.LinkedList;
import java.util.List;
import org.sca.calontir.cmpe.client.FighterInfo;
import org.sca.calontir.cmpe.client.FighterListInfo;

/**
 *
 * @author rikscarborough
 */
public class SearchBar extends Composite {

    private Storage stockStore = null;
    private Button submit;

    public SearchBar(final CellTable<FighterInfo> table, final ListDataProvider<FighterInfo> dataProvider) {
        FlowPanel searchPanel = new FlowPanel();
        DOM.setElementAttribute(searchPanel.getElement(), "id", "searchBar");

        final SuggestBox box = buildSuggestBox();
        DOM.setElementAttribute(box.getElement(), "id", "search");
        DOM.setElementAttribute(box.getElement(), "autocomplete", "off");

        searchPanel.add(box);

        submit = new Button("Lookup", new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                submit.setEnabled(false);
                DOM.getElementById("Signup-Form").getStyle().setDisplay(Style.Display.NONE);


                String searchName = box.getText();
                stockStore = Storage.getLocalStorageIfSupported();
                List<FighterInfo> fighterList = new LinkedList<FighterInfo>();
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
                            if (searchName == null || searchName.isEmpty()
                                    || scaName.stringValue().toUpperCase().contains(searchName.toUpperCase())) {
                                FighterInfo fli = new FighterInfo();
                                fli.setFighterId(new Double(id.doubleValue()).longValue());
                                fli.setScaName(scaName.stringValue());
                                fli.setAuthorizations(auths.stringValue());
                                fli.setGroup(group.stringValue());
                                fighterList.add(fli);
                            }
                        }
                    } catch (Exception e) {
                        Window.alert(e.getMessage());
                    }

                }

                table.setRowCount(fighterList.size());
                List data = dataProvider.getList();
                data.clear();
                for (FighterInfo fli : fighterList) {
                    data.add(fli);
                }

                submit.setEnabled(true);
                DOM.getElementById("List-Box").getStyle().setDisplay(Style.Display.INLINE_BLOCK);
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
