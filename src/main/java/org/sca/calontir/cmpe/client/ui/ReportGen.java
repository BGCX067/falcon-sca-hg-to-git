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
import org.sca.calontir.cmpe.client.ui.qtrlyreport.Activities;
import org.sca.calontir.cmpe.client.ui.qtrlyreport.InjuryReport;
import org.sca.calontir.cmpe.client.ui.qtrlyreport.PersonalInfo;
import org.sca.calontir.cmpe.client.ui.qtrlyreport.Summary;
import org.sca.calontir.cmpe.client.ui.qtrlyreport.Welcome;
import org.sca.calontir.cmpe.client.user.Security;
import org.sca.calontir.cmpe.client.user.SecurityFactory;
import org.sca.calontir.cmpe.common.UserRoles;

/**
 *
 * @author rikscarborough
 */
public class ReportGen extends Composite {

	final private Security security = SecurityFactory.getSecurity();

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

		Activities activities = new Activities();
		activities.init();
		deck.add(activities);

		if (security.isRoleOrGreater(UserRoles.GROUP_MARSHAL)) {
			InjuryReport injuryReport = new InjuryReport();
			injuryReport.init();
			deck.add(injuryReport);
		}

		Summary summary = new Summary();
		summary.init();
		deck.add(summary);


		Panel background = new FlowPanel();
		background.add(deck);
		deck.showWidget(0);

		background.add(buildNextLink(deck));


		initWidget(background);
	}

	private Anchor buildNextLink(final DeckPanel deck) {
		final Anchor nextLink = new Anchor("Next >>");
		nextLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int index = deck.getVisibleWidget();
				if (index < deck.getWidgetCount() - 1) {
					index++;
					deck.showWidget(index);
				} else {
					nextLink.setEnabled(false);
				}
			}
		});

		return nextLink;
	}
}
