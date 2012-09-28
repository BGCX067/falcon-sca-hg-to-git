/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
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
		final DeckPanel deck = new DeckPanel();
		History.newItem("qrtlyreport:Welcome");


		Welcome welcome = new Welcome();
		welcome.init();
		deck.add(welcome);


		PersonalInfo pi = new PersonalInfo();
		pi.init();
		pi.getElement().setId("personalinfo");
		pi.getElement().getStyle().setDisplay(Style.Display.NONE);

		deck.add(pi);


		Panel background = new FlowPanel();
		background.add(deck);
		deck.showWidget(0);

		background.add(buildNextLink(deck));

		
		initWidget(background);
	}

	private Anchor buildNextLink(final DeckPanel deck) {
		Anchor nextLink = new Anchor("Next >>");
		nextLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				int index = deck.getVisibleWidget();
				index++;
				if(index == deck.getWidgetCount()) {
					index = 0;
				}
				deck.showWidget(index);
			}
		});

		return nextLink;
	}
	
}
