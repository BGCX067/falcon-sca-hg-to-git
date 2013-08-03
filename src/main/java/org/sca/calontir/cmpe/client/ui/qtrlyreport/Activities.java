package org.sca.calontir.cmpe.client.ui.qtrlyreport;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import org.sca.calontir.cmpe.client.FighterInfo;
import org.sca.calontir.cmpe.client.FighterService;
import org.sca.calontir.cmpe.client.FighterServiceAsync;
import org.sca.calontir.cmpe.client.ui.LookupController;
import org.sca.calontir.cmpe.client.user.Security;
import org.sca.calontir.cmpe.client.user.SecurityFactory;
import org.sca.calontir.cmpe.common.UserRoles;

/**
 *
 * @author rikscarborough
 */
public class Activities extends BaseReportPage {

    private class ActiveFighter extends Composite {

        public ActiveFighter() {
            Integer retVal = 0;
            List<FighterInfo> fighterList = LookupController.getInstance().getFighterList(null);
            FighterInfo userInfo = LookupController.getInstance().getFighter(security.getLoginInfo().getFighterId());
            for (FighterInfo fli : fighterList) {
                if (fli.getGroup().equals(userInfo.getGroup())) {
                    ++retVal;
                }
            }
            initWidget(new Label(retVal.toString()));
            addReportInfo("Active Fighters", retVal.toString());
        }
    }

    private class MinorFighters extends Composite {

        public MinorFighters() {
            FighterInfo userInfo = LookupController.getInstance().getFighter(security.getLoginInfo().getFighterId());
            final Label minors = new Label();

            FighterServiceAsync fighterService = GWT.create(FighterService.class);
            fighterService.getMinorTotal(userInfo.getGroup(), new AsyncCallback<Integer>() {
                @Override
                public void onFailure(Throwable caught) {
                    log.severe("getMinorTotal: " + caught.getMessage());
                }

                @Override
                public void onSuccess(Integer result) {
                    minors.setText(result.toString());
                    addReportInfo("Minor Fighters", result.toString());
                }
            });

            initWidget(minors);
        }
    }

    private static final Logger log = Logger.getLogger(Activities.class.getName());
    final private Security security = SecurityFactory.getSecurity();
    final private HTML para1 = new HTML();
    final Panel bk = new FlowPanel();
    final Panel persInfo = new FlowPanel();
    RichTextArea activities;

    @Override
    public void buildPage() {
        bk.setStylePrimaryName(REPORTBG);

        persInfo.setStylePrimaryName("activitiesInfo");
        bk.add(persInfo);

        para1.setStylePrimaryName(REPORT_INSTRUCTIONS);
        bk.add(para1);

        activities = new RichTextArea();
        activities.addStyleName(REPORT_TEXT_BOX);
        bk.add(activities);
        addRequired("Activities");
        activities.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                addReportInfo("Activities", activities.getHTML());
            }
        });
        activities.addKeyPressHandler(new RequiredFieldKeyPressHandler("Activities"));

        add(bk);
    }

    @Override
    public void onDisplay() {
        final String p1;
        String reportType = (String) getReportInfo().get("Report Type");
        log.info("Report type = " + reportType);
        if (reportType.equals("Event")) {
            p1 = "Please describe the activities that took place at this event. Tournaments, pickup fights, melees, and what generally occured.";
            buildEventInfo();
            if (getReportInfo().containsKey("Event Name")
                    && getReportInfo().containsKey("Event Date")
                    && getReportInfo().containsKey("Activities")) {
                nextButton.setEnabled(true);
            } else {
                nextButton.setEnabled(false);
            }
        } else {
            p1 = "Please describe your activities as a Marshal for this quarter. Include events you have attended in general, fighter practices in which you are active, and events where you may have assisted in Marshalatte activities.";
            buildQuarterlyInfo();
            if (!activities.getHTML().isEmpty()) {
                nextButton.setEnabled(true);
            } else {
                nextButton.setEnabled(false);
            }
        }
        para1.setHTML(p1);
    }
    TextBox eventName = null;
    DateBox eventDate = null;

    private void buildEventInfo() {
        persInfo.clear();
        Label eventNameLabel = new Label();
        eventNameLabel.setText("Name of the event");
        persInfo.add(eventNameLabel);

        eventName = new TextBox();
        eventName.setName("eventName");
        eventName.getElement().setId("eventName");
        eventName.setValue((String) getReportInfo().get("Event Name"), false);
        addRequired("Event Name");
        eventName.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                addReportInfo("Event Name", event.getValue());
            }
        });
        persInfo.add(eventName);

        persInfo.add(new Label("Date of Event"));
        eventDate = new DateBox();
        eventDate.getTextBox().getElement().setId("eventDate");
        eventDate.getTextBox().setName("eventDate");
        if (getReportInfo().containsKey("Event Date")) {
            eventDate.setValue(DateTimeFormat.getFormat("MM/dd/yyyy").parse((String) getReportInfo().get("Event Date")));
        }
        eventDate.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("MM/dd/yyyy")));
        addRequired("Event Date");
        eventDate.getTextBox().addKeyPressHandler(new RequiredFieldKeyPressHandler("Event Date"));
        eventDate.addValueChangeHandler(new ValueChangeHandler<Date>() {
            @Override
            public void onValueChange(ValueChangeEvent<Date> event) {
                addReportInfo("Event Date", eventDate.getTextBox().getValue());
            }
        });
        persInfo.add(eventDate);

    }

    @Override
    public boolean enableNext() {
        if (eventName != null && eventDate != null) {
            if (eventName.getText().isEmpty() || eventDate.getTextBox().getText().isEmpty() || activities.getHTML().isEmpty()) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private void buildQuarterlyInfo() {
        persInfo.clear();
        if (security.isRole(UserRoles.KNIGHTS_MARSHAL)) {
            Label authFightersLabel = new Label();
            authFightersLabel.setText("Number of Authorized Fighters: ");
            persInfo.add(authFightersLabel);

            persInfo.add(new ActiveFighter());

            Label minorFightersLabel = new Label();
            minorFightersLabel.setText("Number of Minor Fighters: ");
            persInfo.add(minorFightersLabel);

            persInfo.add(new MinorFighters());
        }
    }

    @Override
    public void onLeavePage() {
    }
}
