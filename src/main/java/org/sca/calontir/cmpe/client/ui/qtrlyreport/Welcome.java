package org.sca.calontir.cmpe.client.ui.qtrlyreport;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RadioButton;
import java.util.Date;
import org.sca.calontir.cmpe.client.user.Security;
import org.sca.calontir.cmpe.client.user.SecurityFactory;
import org.sca.calontir.cmpe.common.ReportingMarshalType;
import org.sca.calontir.cmpe.common.UserRoles;

/**
 *
 * @author rikscarborough
 */
public class Welcome extends BaseReportPage {

    final private Security security = SecurityFactory.getSecurity();

    @Override
    public void buildPage() {
        clear();

        Panel bk = new FlowPanel();
        bk.setStylePrimaryName(REPORTBG);
        String p1 = "<h1>Marshal Report Form</h1>";
        HTML para1 = new HTML(p1);
        para1.setStylePrimaryName(REPORT_TITLE);
        bk.add(para1);

        String p2;
        if (security.isRole(UserRoles.DEPUTY_EARL_MARSHAL)) {
            p2 = "To complete Deputy Earl Marshal Report, press the Next>> button then follow the instructions.";
            addReportInfo("Marshal Type", "Deputy Earl Marshal");
        } else if (security.isRole(UserRoles.KNIGHTS_MARSHAL)) {
            p2 = "To complete Group Marshal Report, press the Next>> button then follow the instructions.";
            addReportInfo("Marshal Type", "Group Marshal");
        } else if (security.isRole(UserRoles.CUT_AND_THRUST_MARSHAL)) {
            p2 = "To complete Cut and Thrust Marshal Report, press the Next>> button then follow the instructions.";
            addReportInfo("Marshal Type", "Cut and Thrust Marshal");
        } else {
            p2 = "To complete Marshal of the Field Report, press the Next>> button then follow the instructions.";
            addReportInfo("Marshal Type", "Marshal of the Field");
        }
        p2 = p2 + " Please note, the correct quarter should be selected; do not change unless you need a different quarter.";
        HTML para2 = new HTML(p2);
        para2.setStylePrimaryName(REPORT_INSTRUCTIONS);
        bk.add(para2);

        Panel marshalTypePanel = new HorizontalPanel();
        marshalTypePanel.setStylePrimaryName(REPORT_BUTTON_PANEL);

        final RadioButton armoredCombatButton = new RadioButton("marshalType", ReportingMarshalType.ARMORED_COMBAT.getValue());
        armoredCombatButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue()) {
                    addReportInfo("Reporting Marshal Type", ReportingMarshalType.ARMORED_COMBAT.getCode());
                }
            }
        });
        armoredCombatButton.setValue(Boolean.TRUE, true);
        marshalTypePanel.add(armoredCombatButton);

        final RadioButton calonSteelButton = new RadioButton("marshalType", ReportingMarshalType.CALON_STEEL.getValue());
        calonSteelButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue()) {
                    addReportInfo("Reporting Marshal Type", ReportingMarshalType.CALON_STEEL.getCode());
                }
            }
        });
        calonSteelButton.setValue(Boolean.FALSE, true);
        marshalTypePanel.add(calonSteelButton);

        bk.add(marshalTypePanel);

        int quarter = getQuarter();

        Panel qtrButtonPanel = new HorizontalPanel();
        qtrButtonPanel.setStylePrimaryName(REPORT_BUTTON_PANEL);
        RadioButton qtr1Button = new RadioButton("reportType", "1st Quarter (April 15th)");
        qtr1Button.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue()) {
                    addReportInfo("Report Type", "1st Quarter");
                }
            }
        });
        qtrButtonPanel.add(qtr1Button);

        RadioButton qtr2Button = new RadioButton("reportType", "2nd Quarter (July 15th)");
        qtr2Button.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue()) {
                    addReportInfo("Report Type", "2nd Quarter");
                }
            }
        });
        qtrButtonPanel.add(qtr2Button);

        RadioButton qtr3Button = new RadioButton("reportType", "3rd Quarter (October 15th)");
        qtr3Button.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue()) {
                    addReportInfo("Report Type", "3rd Quarter");
                }
            }
        });
        qtrButtonPanel.add(qtr3Button);

        RadioButton qtr4Button = new RadioButton("reportType", "4th Quarter/Domesday (January 15th)");
        qtr4Button.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue()) {
                    addReportInfo("Report Type", "4th Quarter/Domesday");
                }
            }
        });
        qtrButtonPanel.add(qtr4Button);

        switch (quarter) {
            case 0:
                qtr1Button.setValue(Boolean.TRUE, true);
                break;
            case 1:
                qtr2Button.setValue(Boolean.TRUE, true);
                break;
            case 2:
                qtr3Button.setValue(Boolean.TRUE, true);
                break;
            case 3:
                qtr4Button.setValue(Boolean.TRUE, true);
                break;
            default:
                qtr4Button.setValue(Boolean.TRUE, true);
        }

        bk.add(qtrButtonPanel);

        Panel eventButtonPanel = new HorizontalPanel();
        eventButtonPanel.setStylePrimaryName(REPORT_BUTTON_PANEL);
        RadioButton eventButton = new RadioButton("reportType", "Event Report");
        eventButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue()) {
                    addReportInfo("Report Type", "Event");
                }
            }
        });
        eventButtonPanel.add(eventButton);
        bk.add(eventButtonPanel);

        add(bk);
    }

    @SuppressWarnings("deprecation")
    private int getQuarter() {
        Date now = new Date();
        if (now.getMonth() < 2 || now.getMonth() == 11) {
            return 3;
        }

        if (now.getMonth() < 5) {
            return 0;
        }

        if (now.getMonth() < 7) {
            return 1;
        }

        if (now.getMonth() < 11) {
            return 2;
        }
        return 0;
    }

    @Override
    public void onDisplay() {
        nextButton.setEnabled(true);
    }

    @Override
    public void onLeavePage() {
        String reportType = (String) getReportInfo().get("Report Type");
        if (reportType.equals("Event")) {
            InjuryReport injuryReport = new InjuryReport();
            injuryReport.init(reportInfo, required, submitButton, nextButton);
            getDeck().insert(injuryReport, getDeck().getWidgetCount() - 2);
        } else {
            if (security.isRole(UserRoles.KNIGHTS_MARSHAL)
                    || security.isRole(UserRoles.DEPUTY_EARL_MARSHAL)) {
                InjuryReport injuryReport = new InjuryReport();
                injuryReport.init(reportInfo, required, submitButton, nextButton);
                getDeck().insert(injuryReport, getDeck().getWidgetCount() - 2);

                FighterComment fc = new FighterComment();
                fc.init(reportInfo, required, submitButton, nextButton);
                getDeck().insert(fc, getDeck().getWidgetCount() - 2);
            }
        }

    }
}
