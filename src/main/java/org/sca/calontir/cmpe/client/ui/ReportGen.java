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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Panel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.sca.calontir.cmpe.client.DisplayUtils;
import org.sca.calontir.cmpe.client.FighterService;
import org.sca.calontir.cmpe.client.FighterServiceAsync;
import org.sca.calontir.cmpe.client.ui.qtrlyreport.Activities;
import org.sca.calontir.cmpe.client.ui.qtrlyreport.BaseReportPage;
import org.sca.calontir.cmpe.client.ui.qtrlyreport.FighterComment;
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
        final DeckPanel deck = new DeckPanel();
        deck.setAnimationEnabled(true);
        History.newItem("qrtlyreport:Welcome");

        final FocusWidget next = buildNextLink(deck);
        next.setEnabled(true);

        final Button submit = new Button("Submit Report");
        submit.setEnabled(false);
        submit.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        submit.getElement().getStyle().setDisplay(Style.Display.NONE);
        submit.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (submit.isEnabled()) {
                    submit.setEnabled(false);
                    FighterServiceAsync fighterService = GWT.create(FighterService.class);
                    fighterService.sendReportInfo(reportInfo, new AsyncCallback<Void>() {
                        @Override
                        public void onFailure(Throwable caught) {
                        }

                        @Override
                        public void onSuccess(Void result) {
                            LookupController.getInstance().updateLocalData();
                            Shout shout = Shout.getInstance();
                            shout.tell("Thank you for submitting your report");
                            DisplayUtils.resetDisplay();
                        }
                    });
                }
            }
        });

        //reportInfo.put("Email Cc", user.);
        reportInfo.put("user.googleid", security.getLoginInfo().getEmailAddress());
        List<String> required = new ArrayList<String>();

        Welcome welcome = new Welcome();
        welcome.init(reportInfo, required, submit, next);
        welcome.setDeck(deck);
        deck.add(welcome);


        PersonalInfo pi = new PersonalInfo();
        pi.init(reportInfo, required, submit, next);
        pi.getElement().setId("personalinfo");
        pi.getElement().getStyle().setDisplay(Style.Display.NONE);
        deck.add(pi);

        Activities activities = new Activities();
        activities.init(reportInfo, required, submit, next);
        deck.add(activities);

        if (security.isRole(UserRoles.KNIGHTS_MARSHAL)
                || security.isRole(UserRoles.DEPUTY_EARL_MARSHAL)) {
            InjuryReport injuryReport = new InjuryReport();
            injuryReport.init(reportInfo, required, submit, next);
            deck.add(injuryReport);

            FighterComment fc = new FighterComment();
            fc.init(reportInfo, required, submit, next);
            deck.add(fc);
        }

        Summary summary = new Summary();
        summary.init(reportInfo, required, submit, next);
        deck.add(summary);

        Final finalPage = new Final();
        finalPage.init(reportInfo, required, submit, next);
        deck.add(finalPage);

        Panel background = new FlowPanel();

        background.add(deck);
        deck.showWidget(0);

        background.add(buildPrevLink(deck));
        background.add(next);
        background.add(submit);


        initWidget(background);
    }

    private FocusWidget buildNextLink(final DeckPanel deck) {
        final Button nextLink = new Button("Next >>");
        nextLink.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (nextLink.isEnabled()) {
                    int index = deck.getVisibleWidget();
                    if (index < deck.getWidgetCount() - 1) {
                        if (index >= 0) {
                            BaseReportPage prevPage = (BaseReportPage) deck.getWidget(index);
                            prevPage.onLeavePage();
                        }
                        index++;
                        BaseReportPage nextPage = (BaseReportPage) deck.getWidget(index);
                        nextPage.onDisplay();
                        deck.showWidget(index);
                    }
                }
            }
        });
        nextLink.setWidth("90px");
//		nextLink.setHeight(".90em");
//		nextLink.getElement().getStyle().setFontSize(0.75, Style.Unit.EM);

        return nextLink;
    }

    private FocusWidget buildPrevLink(final DeckPanel deck) {
        final Button prevLink = new Button("<< Prev");
        prevLink.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (prevLink.isEnabled()) {
                    int index = deck.getVisibleWidget();
                    if (index == 1) {
                        Shout.getInstance().tell("To reselect the report type, select the Report link from the menu and start again.");
                    } else if (index > 1) {
                        if (index < deck.getWidgetCount()) {
                            BaseReportPage prevPage = (BaseReportPage) deck.getWidget(index);
                            prevPage.onLeavePage();
                        }
                        --index;
                        BaseReportPage nextPage = (BaseReportPage) deck.getWidget(index);
                        nextPage.onDisplay();
                        deck.showWidget(index);
                    }
                }
            }
        });
        prevLink.setWidth("90px");
//		nextLink.setHeight(".90em");
//		nextLink.getElement().getStyle().setFontSize(0.75, Style.Unit.EM);

        return prevLink;
    }
}
