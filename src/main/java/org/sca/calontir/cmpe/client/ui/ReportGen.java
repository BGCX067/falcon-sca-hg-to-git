/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TabPanel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.sca.calontir.cmpe.client.DisplayUtils;
import org.sca.calontir.cmpe.client.FighterService;
import org.sca.calontir.cmpe.client.FighterServiceAsync;
import org.sca.calontir.cmpe.client.ui.qtrlyreport.Activities;
import org.sca.calontir.cmpe.client.ui.qtrlyreport.Final;
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
	Map<String, Object> reportInfo = new HashMap<String, Object>();

	public void init() {
		//final DeckPanel deck = new DeckPanel();
		final TabPanel deck = new TabPanel();
		History.newItem("qrtlyreport:Welcome");

		final Button submit = new Button("Submit Report");
		submit.setEnabled(false);
		submit.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
		submit.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (submit.isEnabled()) {
					FighterServiceAsync fighterService = GWT.create(FighterService.class);
					fighterService.sendReportInfo(reportInfo, new AsyncCallback<Void>() {
						@Override
						public void onFailure(Throwable caught) {
						}

						@Override
						public void onSuccess(Void result) {
							Shout shout = Shout.getInstance();
							shout.tell("Thank you for submitting your report");
							DisplayUtils.resetDisplay();
						}
					});
				}
			}
		});

		reportInfo.put("Email From", security.getLoginInfo().getEmailAddress());
		reportInfo.put("Email To", "BrendanMacantSaoir@gmail.com");
		List<String> required = new ArrayList<String>();

		Welcome welcome = new Welcome();
		welcome.init(reportInfo, required, submit);
		deck.add(welcome, "Welcome");


		PersonalInfo pi = new PersonalInfo();
		pi.init(reportInfo, required, submit);
		pi.getElement().setId("personalinfo");
		pi.getElement().getStyle().setDisplay(Style.Display.NONE);
		deck.add(pi, "Personal Info");

		Activities activities = new Activities();
		activities.init(reportInfo, required, submit);
		deck.add(activities, "Activities");

		if (security.isRoleOrGreater(UserRoles.GROUP_MARSHAL)) {
			InjuryReport injuryReport = new InjuryReport();
			injuryReport.init(reportInfo, required, submit);
			deck.add(injuryReport, "Injury Report");
		}

		Summary summary = new Summary();
		summary.init(reportInfo, required, submit);
		deck.add(summary, "Summary");

		Final finalPage = new Final();
		finalPage.init(reportInfo, required, submit);
		deck.add(finalPage, "Final Page");

		Panel background = new FlowPanel();

		background.add(deck);
		deck.selectTab(0);

		background.add(buildNextLink(deck.getDeckPanel()));
		background.add(submit);


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
					if (index >= deck.getWidgetCount() - 1) {
						nextLink.setEnabled(false);
						nextLink.getElement().getStyle().setDisplay(Style.Display.NONE);
					}
					deck.showWidget(index);
				} else {
					nextLink.setEnabled(false);
					nextLink.getElement().getStyle().setDisplay(Style.Display.NONE);
				}
			}
		});
		nextLink.setStylePrimaryName("buttonLink");

		return nextLink;
	}
}
