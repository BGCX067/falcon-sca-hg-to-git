/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import org.sca.calontir.cmpe.client.FighterInfo;
import org.sca.calontir.cmpe.client.user.Security;
import org.sca.calontir.cmpe.client.user.SecurityFactory;

/**
 *
 * @author rikscarborough
 */
public class FighterListBox extends Composite {

    final private Security security = SecurityFactory.getSecurity();

    public FighterListBox(CellTable<FighterInfo> table, ListDataProvider<FighterInfo> dataProvider) {
        Panel listBackground = new FlowPanel();
        listBackground.getElement().setId("List-Box");
        listBackground.getElement().getStyle().setDisplay(Style.Display.NONE);

        Panel listPanel = new FlowPanel();
        listPanel.setStyleName("list");
//        listPanel.getElement().setId("List-Box");
//        listPanel.getElement().getStyle().setDisplay(Style.Display.NONE);

        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        SimplePager pager = new SimplePager(SimplePager.TextLocation.CENTER, pagerResources, false, 10, true);
        pager.setDisplay(table);

        TextColumn<FighterInfo> scaNameColumn = new TextColumn<FighterInfo>() {

            @Override
            public String getValue(FighterInfo fli) {
                return fli.getScaName();
            }
        };
        //TODO: Turning off sorting for now. Once everything else is settled,
        //      revisit this.
        scaNameColumn.setSortable(false);

        TextColumn<FighterInfo> authorizationColumn = new TextColumn<FighterInfo>() {

            @Override
            public String getValue(FighterInfo fli) {
                return fli.getAuthorizations();
            }
        };

        TextColumn<FighterInfo> groupColumn = new TextColumn<FighterInfo>() {

            @Override
            public String getValue(FighterInfo fli) {
                return fli.getGroup();
            }
        };
        groupColumn.setSortable(false);
        table.addColumn(scaNameColumn, "SCA Name");
        table.addColumn(authorizationColumn, "Authorizations");
        table.addColumn(groupColumn, "Group");

        final SingleSelectionModel<FighterInfo> selectionModel = new SingleSelectionModel<FighterInfo>();
        table.setSelectionModel(selectionModel);
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                FighterInfo selected = selectionModel.getSelectedObject();
                if (selected != null) {
                    if (security.canView(selected.getFighterId())) {
//                        Window.open("/FighterSearchServlet?mode=lookup&fid=" + selected.getFighterId(), "_self", "");
                        DOM.getElementById("Signup-Form").getStyle().setDisplay(Style.Display.NONE);
                        DOM.getElementById("List-Box").getStyle().setDisplay(Style.Display.NONE);
                        DOM.getElementById("FighterForm").getStyle().setDisplay(Style.Display.BLOCK);
                        fireEvent(new EditViewEvent(Mode.VIEW, selected));

                    }
                }
            }
        });


        dataProvider.addDataDisplay(table);



        listPanel.add(table);
        listPanel.add(pager);
//        listPanel.add(new HTML("&nbsp;"));

        listBackground.add(listPanel);
        initWidget(listBackground);

    }
}
