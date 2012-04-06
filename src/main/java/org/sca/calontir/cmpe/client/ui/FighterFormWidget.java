/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import java.util.Date;
import java.util.List;
import org.sca.calontir.cmpe.client.user.Security;
import org.sca.calontir.cmpe.client.user.SecurityFactory;
import org.sca.calontir.cmpe.common.UserRoles;
import org.sca.calontir.cmpe.dto.Address;
import org.sca.calontir.cmpe.dto.Authorization;
import org.sca.calontir.cmpe.dto.Fighter;

/**
 *
 * @author rikscarborough
 */
public class FighterFormWidget extends Composite implements EditViewHandler {

    final private Security security = SecurityFactory.getSecurity();
    private Panel overallPanel = new FlowPanel();
    private Panel fighterIdBoxPanel = new FlowPanel();
    private Panel authPanel = new FlowPanel();
    private Panel infoPanel = new FlowPanel();
    private Panel notePanel = new FlowPanel();
    private Fighter fighter;
    private String target;

    public FighterFormWidget() {
        fighterIdBoxPanel.setStyleName("figherIdBox");
        overallPanel.add(fighterIdBoxPanel);

        authPanel.setStyleName("dataBox");
        overallPanel.add(authPanel);

        infoPanel.setStyleName("dataBox");
        overallPanel.add(infoPanel);

//        if (security.isRoleOrGreater(UserRoles.CARD_MARSHAL)) {
            notePanel.setStyleName("dataBoxShort");
            overallPanel.add(notePanel);
//        } hide and turn on during call

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

        if (security.isRoleOrGreater(UserRoles.CARD_MARSHAL)) {
            notePanelView();
        }
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
        if (fighter.getAddress() != null && !fighter.getAddress().isEmpty()) {
            Address address = fighter.getAddress().get(0);
            StringBuilder sb = new StringBuilder();
            sb.append(address.getAddress1());
            if (address.getAddress2() != null && !address.getAddress2().isEmpty()) {
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

        table.setText(2, 0, "SCA Membership:");
        table.setText(2, 1, fighter.getScaMemberNo());
        formatter.setStyleName(2, 0, "label");
        formatter.setStyleName(2, 1, "data");

        table.setText(3, 0, "Group:");
        table.setText(3, 1,
                fighter.getScaGroup() == null ? "" : fighter.getScaGroup().getGroupName());
        formatter.setStyleName(3, 0, "label");
        formatter.setStyleName(3, 1, "data");

        table.setText(4, 0, "Minor:");
        if (fighter.getDateOfBirth() != null) {
            table.setText(4, 1, isMinor(fighter.getDateOfBirth()) ? "true" : "false");
        } else {
            table.setText(4, 1, "false");
        }
        formatter.setStyleName(4, 0, "label");
        formatter.setStyleName(4, 1, "data");

        table.setText(5, 0, "DOB:");
        if (fighter.getDateOfBirth() != null) {
            table.setText(5, 1,
                    DateTimeFormat.getFormat("MM/dd/yyyy").format(fighter.getDateOfBirth()));
        }
        formatter.setStyleName(5, 0, "label");
        formatter.setStyleName(5, 1, "data");

        table.setText(6, 0, "Phone Number:");
        if (fighter.getPhone() != null && !fighter.getPhone().isEmpty()) {
            table.setText(6, 1, fighter.getPhone().get(0).getPhoneNumber());
        }
        formatter.setStyleName(6, 0, "label");
        formatter.setStyleName(6, 1, "data");

        table.setText(7, 0, "Email Address:");
        if (fighter.getEmail() != null && !fighter.getEmail().isEmpty()) {
            table.setText(7, 1, fighter.getEmail().get(0).getEmailAddress());
        }
        formatter.setStyleName(7, 0, "label");
        formatter.setStyleName(7, 1, "data");

        fighterInfo.add(table);

        if (security.isRoleOrGreater(UserRoles.CARD_MARSHAL)) { // or user admin
            Panel adminInfo = new FlowPanel();
            adminInfo.getElement().setId("fighterInfo");
            DOM.setElementAttribute(adminInfo.getElement(), "id", "adminInfo");
            fighterInfo.add(adminInfo);

            FlexTable adminTable = new FlexTable();
            FlexTable.FlexCellFormatter adminFormatter = adminTable.getFlexCellFormatter();

            adminTable.setText(0, 0, "Google ID:");
            adminTable.setText(0, 1, fighter.getGoogleId());
            adminFormatter.setStyleName(0, 0, "label");
            adminFormatter.setStyleName(0, 1, "data");

            adminTable.setText(1, 0, "User Role:");
            if (fighter.getRole() != null) {
                adminTable.setText(1, 1, fighter.getRole().toString());
            }
            adminFormatter.setStyleName(1, 0, "label");
            adminFormatter.setStyleName(1, 1, "data");

            adminInfo.add(adminTable);

        }

        infoPanel.add(dataBody);
    }

    private void notePanelView() {
        notePanel.clear();

        Panel dataHeader = new FlowPanel();
        dataHeader.setStyleName("dataHeader");
        InlineLabel label = new InlineLabel("Notes");
        dataHeader.add(label);

        notePanel.add(dataHeader);

        Panel dataBody = new FlowPanel();
        dataBody.setStyleName("dataBody");
        Label note = new Label();
        if (fighter.getNote() != null) {
            note.setText(fighter.getNote().getBody());
        }
        dataBody.add(note);
        notePanel.add(dataBody);
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

    private boolean isMinor(Date dob) {
        Date now = new Date();
        Date targetDate = new Date(dob.getYear() + 18, dob.getMonth(), dob.getDate());
        if (targetDate.after(now)) {
            return true;
        }
        return false;
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
