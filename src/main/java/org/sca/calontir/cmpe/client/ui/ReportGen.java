/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import org.sca.calontir.cmpe.client.ui.qtrlyreport.PersonalInfo;
import org.sca.calontir.cmpe.client.ui.qtrlyreport.Welcome;

/**
 *
 * @author rikscarborough
 */
public class ReportGen extends Composite {

	public void init() {
		Panel background = new FlowPanel();
		History.newItem("qrtlyreport:Welcom");


		Welcome welcome = new Welcome();
		welcome.init();
		background.add(welcome);


		PersonalInfo pi = new PersonalInfo();
		pi.init();
		pi.getElement().setId("personalinfo");
		pi.getElement().getStyle().setDisplay(Style.Display.NONE);

		
		initWidget(background);
	}
	
}
