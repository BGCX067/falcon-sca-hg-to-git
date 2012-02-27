/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;

/**
 *
 * @author rikscarborough
 */
public class CalonBar  extends Composite {

    public CalonBar() {
        
        Panel barPanel = new FlowPanel();
        barPanel.setStylePrimaryName("calonbar");
        
        Label k = new Label();
        k.setText("Home");
        barPanel.add(k);
        
        initWidget(barPanel);
    }
    
}
