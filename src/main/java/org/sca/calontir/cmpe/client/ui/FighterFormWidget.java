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
import java.util.List;
import org.sca.calontir.cmpe.dto.Address;
import org.sca.calontir.cmpe.dto.Authorization;
import org.sca.calontir.cmpe.dto.Fighter;

/**
 *
 * @author rikscarborough
 */
public class FighterFormWidget extends Composite implements EditViewHandler {

    private Panel overallPanel = new FlowPanel();
    private Panel fighterIdBoxPanel = new FlowPanel();
    private Panel authPanel = new FlowPanel();
    private Panel infoPanel = new FlowPanel();
    private Fighter fighter;
    private String target;

    public FighterFormWidget() {
        fighterIdBoxPanel.setStyleName("figherIdBox");
        overallPanel.add(fighterIdBoxPanel);

        authPanel.setStyleName("dataBox");
        overallPanel.add(authPanel);

        infoPanel.setStyleName("dataBox");
        overallPanel.add(infoPanel);

        initWidget(overallPanel);
    }

    @Override
    public void buildEdit() {
        fighterIdBoxPanel.clear();

        final Hidden fighterId = new Hidden("fighterId");
        fighterId.setValue(fighter.getFighterId().toString());
        fighterIdBoxPanel.add(fighterId);

        fighterIdBoxPanel.add(new InlineLabel("SCA Name:"));

        final TextBox tb = new TextBox();
        tb.setName("scaName");
        tb.setVisibleLength(25);
        tb.setStyleName("scaName");
        tb.setValue(fighter.getScaName());
        fighterIdBoxPanel.add(tb);
    }

    @Override
    public void buildView() {
        fighterIdBoxPanel.clear();


        final Hidden fighterId = new Hidden("fighterId");
        fighterId.setValue(fighter.getFighterId().toString());
        fighterIdBoxPanel.add(fighterId);

        fighterIdBoxPanel.add(new InlineLabel("SCA Name:"));

        InlineLabel scaName = new InlineLabel();
        scaName.setText(fighter.getScaName());
        scaName.setStyleName("scaName");
        fighterIdBoxPanel.add(scaName);

        fighterIdBoxPanel.add(printButton());

        authPanel.clear();

        Panel dataHeader = new FlowPanel();
        dataHeader.setStyleName("dataHeader");
        InlineLabel authLabel = new InlineLabel("Authorizations");
        dataHeader.add(authLabel);

        dataHeader.add(editButton());

        authPanel.add(dataHeader);

        Panel dataBody = new FlowPanel();
        dataBody.setStyleName("dataBody");
        Label auths = new Label();
        auths.setText(getAuthsAsString(fighter.getAuthorization()));
        dataBody.add(auths);
        authPanel.add(dataBody);


        infoPanelView();


    }

    private void infoPanelView() {
        infoPanel.clear();

        Panel dataHeader = new FlowPanel();
        dataHeader.setStyleName("dataHeader");
        InlineLabel authLabel = new InlineLabel("Fighter Info");
        dataHeader.add(authLabel);

        dataHeader.add(editButton());

        infoPanel.add(dataHeader);

        Panel dataBody = new FlowPanel();
        dataBody.setStyleName("dataBody");

        Panel fighterInfo = new FlowPanel();
        fighterInfo.getElement().setId("fighterInfo");
        DOM.setElementAttribute(fighterInfo.getElement(), "id", "fighterInfo");
        dataBody.add(fighterInfo);

        FlexTable table = new FlexTable();
        FlexTable.FlexCellFormatter formatter = table.getFlexCellFormatter();
        table.setStyleName("wide-table");
        table.setText(0, 0, "Modern Name:");
        table.setText(0, 1, fighter.getModernName());
        table.setText(0, 2, fighter.getStatus().toString());
        formatter.setStyleName(0, 0, "label");
        formatter.setStyleName(0, 1, "data");
        formatter.setStyleName(0, 2, "rightCol");

        table.setText(1, 0, "Address:");
        if(fighter.getAddress() != null && !fighter.getAddress().isEmpty()) {
            Address address = fighter.getAddress().get(0);
            StringBuilder sb = new StringBuilder();
            sb.append(address.getAddress1());
            if(address.getAddress2() != null && !address.getAddress2().isEmpty()) {
                sb.append(", ");
                sb.append(address.getAddress2());
            }
            sb.append(", ");
            sb.append(address.getCity());
            sb.append(", ");
            sb.append(address.getState());
            sb.append("&nbsp;&nbsp;");
            sb.append(address.getPostalCode());
            table.setHTML(1, 1, sb.toString());
        }
        
        
        formatter.setStyleName(1, 0, "label");
        formatter.setStyleName(1, 1, "data");
        if (fighter.getTreaty() != null) {
            table.setText(1, 2, fighter.getTreaty().getName());
            formatter.setStyleName(1, 2, "rightCol");
        }

        fighterInfo.add(table);

        infoPanel.add(dataBody);
    }

    @Override
    public void buildAdd() {
        fighterIdBoxPanel.clear();

        fighterIdBoxPanel.add(new InlineLabel("SCA Name:"));

        final TextBox tb = new TextBox();
        tb.setName("scaName");
        tb.setVisibleLength(25);
        tb.setStyleName("scaName");
        fighterIdBoxPanel.add(tb);

        fighterIdBoxPanel.add(printButton());
    }

    @Override
    public void setFighter(Fighter fighter) {
        this.fighter = fighter;
    }

    private Widget printButton() {
        Anchor bPrint = new Anchor("Print");
        bPrint.setStyleName("BPrint");
        bPrint.getElement().setId("BPrint");

        bPrint.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                Window.alert("click");
            }
        });

        return bPrint;
    }

    private Widget editButton() {
        Anchor editButton = new Anchor("Edit");
        editButton.setStyleName("button");
        editButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                Window.alert("click");
            }
        });


        return editButton;
    }

    private String getAuthsAsString(List<Authorization> authorizations) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        if (authorizations != null) {
            for (Authorization a : authorizations) {
                if (first) {
                    first = false;
                } else {
                    sb.append(" ; ");
                }
                sb.append(a.getCode());
            }
        }

        return sb.toString();
    }
}
