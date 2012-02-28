/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;

/**
 *
 * @author rikscarborough
 */
public class SearchBar extends Composite {

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

        MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
        oracle.add("Rik");
        oracle.add("Brendan");
        oracle.add("Brendan Mac");
        SuggestBox box = new SuggestBox(oracle);
        DOM.setElementAttribute(box.getElement(), "id", "search");

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
}
