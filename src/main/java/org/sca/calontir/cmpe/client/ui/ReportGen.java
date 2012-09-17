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
public class ReportGen extends Composite {

	public void init() {
		Panel background = new FlowPanel();
		Label label = new Label("Report Generator");

		background.add(label);
		
		initWidget(background);
	}
	
}
