package org.sca.calontir.cmpe.client.ui.qtrlyreport;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextArea;

/**
 *
 * @author rikscarborough
 */
public class Summary extends BaseReportPage {
	public static final String SUMMARY = "Summary";
	private HTML para1 = new HTML();

	@Override
	public void buildPage() {
		final Panel bk = new FlowPanel();
		bk.setStylePrimaryName(REPORTBG);

		para1.setStylePrimaryName(REPORT_INSTRUCTIONS);
		bk.add(para1);

		final TextArea summary = new TextArea();
		summary.setStylePrimaryName(REPORT_TEXT_BOX);
		bk.add(summary);
		addRequired(SUMMARY);
		summary.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				addReportInfo(SUMMARY, event.getValue());
			}
		});
		summary.addKeyPressHandler(requiredFieldKeyPressHandler);

		add(bk);
	}

	@Override
	public void onDisplay() {
		nextButton.setEnabled(false);
		String p1;
		String reportType = (String) getReportInfo().get("Report Type");
		if (reportType.equals("Event")) {
			p1 = "Enter a summary of your report for this event.";
		} else {
			p1 = "Enter a summary of your report for this quarter.";
		}
		para1.setHTML(p1);
	}

	@Override
	public void onLeavePage() {
	}
	
}
