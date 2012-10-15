package org.sca.calontir.cmpe.client.ui.qtrlyreport;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RadioButton;
import java.util.Map;
import org.sca.calontir.cmpe.client.user.Security;
import org.sca.calontir.cmpe.client.user.SecurityFactory;
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
		String p1 = "<h1>Marshal Report Form</h1>";
		HTML para1 = new HTML(p1);
		para1.setStyleName("reportTitle");
		bk.add(para1);

		String p2;
		if (security.isRole(UserRoles.DEPUTY_EARL_MARSHAL)) {
			p2 = "To complete Deputy Earl Marshal Report, press the Next>> button then follow the instructions.";
			addReportInfo("Marshal Type", "Deputy Earl Marshal");
		} else if (security.isRole(UserRoles.GROUP_MARSHAL) || security.isRole(UserRoles.KNIGHTS_MARSHAL)) {
			p2 = "To complete Group Marshal Report, press the Next>> button then follow the instructions.";
			addReportInfo("Marshal Type", "Group Marshal");
		} else {
			p2 = "To complete Marshal of the Field Report, press the Next>> button then follow the instructions.";
			addReportInfo("Marshal Type", "Marshal of the Field");
		}
		HTML para2 = new HTML(p2);
		para2.setStyleName("reportBody");
		bk.add(para2);

		final Map<String, Object> reportInfo = getReportInfo();

		Panel qtrButtonPanel = new HorizontalPanel();
		RadioButton qtr1Button = new RadioButton("reportType", "1st Quarter");
		qtr1Button.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					addReportInfo("Report Type", "1st Quarter");
				}
			}
		});
		qtrButtonPanel.add(qtr1Button);

		RadioButton qtr2Button = new RadioButton("reportType", "2nd Quarter");
		qtr2Button.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					addReportInfo("Report Type", "2nd Quarter");
				}
			}
		});
		qtrButtonPanel.add(qtr2Button);

		RadioButton qtr3Button = new RadioButton("reportType", "3rd Quarter");
		qtr3Button.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					addReportInfo("Report Type", "2rd Quarter");
				}
			}
		});
		qtrButtonPanel.add(qtr3Button);

		RadioButton qtr4Button = new RadioButton("reportType", "4th Quarter/Domesday");
		qtr4Button.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					addReportInfo("Report Type", "4th Quarter/Domesday");
				}
			}
		});
		qtrButtonPanel.add(qtr4Button);


		bk.add(qtrButtonPanel);

		Panel eventButtonPanel = new HorizontalPanel();
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
}
