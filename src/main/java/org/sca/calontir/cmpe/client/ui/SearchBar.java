/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.*;

/**
 *
 * @author rikscarborough
 */
public class SearchBar extends Composite {

    private Storage stockStore = null;
    private Hidden mode = new Hidden("mode");
    private Button submit;

    public SearchBar() {
        final FormPanel searchForm = new FormPanel();
        searchForm.setAction("/fighterg");
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

        submit = new Button("Submit", new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                mode.setValue("search");
                searchForm.submit();
            }
        });

        searchPanel.add(submit);


        searchForm.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {

            @Override
            public void onSubmitComplete(SubmitCompleteEvent event) {
                Window.alert(event.getResults());
                DOM.getElementById("List-Box").getStyle().setDisplay(Style.Display.INLINE);
                submit.setEnabled(true);
            }
        });

        searchForm.addSubmitHandler(new FormPanel.SubmitHandler() {

            @Override
            public void onSubmit(SubmitEvent event) {
                submit.setEnabled(false);
                DOM.getElementById("Signup-Form").getStyle().setDisplay(Style.Display.NONE);
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
