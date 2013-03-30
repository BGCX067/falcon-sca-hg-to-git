package org.sca.calontir.cmpe.client.ui.qtrlyreport;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RichTextArea;

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

		final RichTextArea summary = new RichTextArea();
		summary.addStyleName(REPORT_TEXT_BOX);
		bk.add(summary);
		addRequired(SUMMARY);
		summary.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				addReportInfo(SUMMARY, summary.getHTML());
			}
		});
		summary.addKeyPressHandler(new RequiredFieldKeyPressHandler(SUMMARY));

		add(bk);
	}

	@Override
	public void onDisplay() {
		if(getReportInfo().containsKey(SUMMARY)) {
			nextButton.setEnabled(true);
		} else {
			nextButton.setEnabled(false);
		}
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
