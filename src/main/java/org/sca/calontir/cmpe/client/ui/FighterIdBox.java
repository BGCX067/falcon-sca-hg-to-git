package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import org.sca.calontir.cmpe.client.FighterInfo;
import org.sca.calontir.cmpe.client.FighterListInfo;
import org.sca.calontir.cmpe.client.FighterService;
import org.sca.calontir.cmpe.client.FighterServiceAsync;
import org.sca.calontir.cmpe.dto.Fighter;

/**
 *
 * @author rikscarborough
 */
public class FighterIdBox extends Composite implements EditViewHandler {

    private FlowPanel figherIdBoxPanel = new FlowPanel();
    private Fighter fighter;

    public FighterIdBox() {
        figherIdBoxPanel.setStyleName("figherIdBox");


        initWidget(figherIdBoxPanel);
    }

    public void setFighter(Fighter fighter) {
        this.fighter = fighter;
    }

    @Override
    public void buildEdit() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void buildView() {
        figherIdBoxPanel.clear();


        final Hidden fighterId = new Hidden("fighterId");
        fighterId.setValue(fighter.getFighterId().toString());
        figherIdBoxPanel.add(fighterId);

        InlineLabel scaName = new InlineLabel();
        scaName.setText(fighter.getScaName());
        scaName.setStyleName("scaName");
        figherIdBoxPanel.add(scaName);

        figherIdBoxPanel.add(printButton());
    }

    @Override
    public void buildAdd() {
        figherIdBoxPanel.clear();

        final TextBox tb = new TextBox();
        tb.setName("scaName");
        tb.setVisibleLength(25);
        tb.setStyleName("scaName");
        figherIdBoxPanel.add(tb);

        figherIdBoxPanel.add(printButton());
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
}
