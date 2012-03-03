/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.storage.client.StorageMap;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import java.lang.reflect.Array;
import java.util.List;
import org.sca.calontir.cmpe.client.FighterListInfo;
import org.sca.calontir.cmpe.client.FighterService;
import org.sca.calontir.cmpe.client.FighterServiceAsync;

/**
 *
 * @author rikscarborough
 */
public class SearchBar extends Composite {

    private Storage stockStore = null;
    private Hidden mode = new Hidden("mode");

    public SearchBar() {
        final FormPanel searchForm = new FormPanel();
        searchForm.setAction("/FighterSearchServlet");
        searchForm.setMethod(FormPanel.METHOD_POST);



        FlowPanel searchPanel = new FlowPanel();
        DOM.setElementAttribute(searchPanel.getElement(), "id", "searchBar");
        searchForm.setWidget(searchPanel);

        mode.setID("mode");
        mode.setDefaultValue("");

        SuggestBox box = buildSuggestBox();
        DOM.setElementAttribute(box.getElement(), "id", "search");
        DOM.setElementAttribute(box.getElement(), "autocomplete", "off");

        searchPanel.add(box);

        searchPanel.add(new Button("Submit", new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                mode.setValue("search");
                searchForm.submit();
            }
        }));


        searchForm.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {

            @Override
            public void onSubmitComplete(SubmitCompleteEvent event) {
            }
        });

        searchForm.addSubmitHandler(new FormPanel.SubmitHandler() {

            @Override
            public void onSubmit(SubmitEvent event) {
                // Verify here.
            }
        });

        initWidget(searchForm);
    }

    private SuggestBox buildSuggestBox() {
        final MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
        stockStore = Storage.getLocalStorageIfSupported();
        if (stockStore != null) {
            String scaNameListStr = stockStore.getItem("scaNameList");
            if (scaNameListStr != null && scaNameListStr.trim().length() > 0) {
                String[] scaNameArray = scaNameListStr.split(";");
                for (int i = 0; i < scaNameArray.length; ++i) {
                    oracle.add(scaNameArray[i]);
                }
            }
        }



        return new SuggestBox(oracle);
    }
}
