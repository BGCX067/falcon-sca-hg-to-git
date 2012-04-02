/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.sca.calontir.cmpe.client.FighterInfo;

/**
 *
 * @author rikscarborough
 */
public class FighterIdBoxEdit extends Composite implements EditViewHandler {
    private FlowPanel figherIdBoxPanel = new FlowPanel();
    private FighterInfo fighter;
    

    public FighterIdBoxEdit() {        
        figherIdBoxPanel.setStyleName("figherIdBox");


        initWidget(figherIdBoxPanel);


    }

    public void setFighter(FighterInfo fighter) {
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
        
        Label scaName = new Label();
        scaName.setText(fighter.getScaName());
        figherIdBoxPanel.add(scaName);
    }

    @Override
    public void buildAdd() {
        figherIdBoxPanel.clear();
        
        final TextBox tb = new TextBox();
        tb.setName("scaName");
        tb.setVisibleLength(25);
        tb.setStyleName("scaName");
        figherIdBoxPanel.add(tb);

//        InlineHyperlink printButton = new InlineHyperlink();
//        printButton.setText("Print");
//        printButton.setStyleName("printButton");
//        
//        figherIdBoxPanel.add(printButton);
//        
//        printButton.addClickHandler(new ClickHandler() {
//
//            @Override
//            public void onClick(ClickEvent event) {
//                Window.alert("click");
//            }
//        });

        Anchor bPrint = new Anchor("Print");
        bPrint.setStyleName("BPrint");
        bPrint.getElement().setId("BPrint");

        figherIdBoxPanel.add(bPrint);

        bPrint.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                Window.alert("click");
            }
        });
    }
}
