package org.sca.calontir.cmpe.client.ui.qtrlyreport;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextArea;

/**
 *
 * @author rikscarborough
 */
public class InjuryReport extends BaseReportPage {

	@Override
	public void buildPage() {
		final Panel bk = new FlowPanel();
		bk.setStylePrimaryName(REPORTBG);

		final String p1;
		String reportType = (String) getReportInfo().get("Report Type");
		if(reportType.equals("Event")) {
			p1 = "Please include detailed information on any injuries, fighter disciplinary issues, Marshal Courts, or any other issues for this event.  Please include specifics such as individuals involved, etc.";
		} else {
			p1 = "Please include detailed information on any injuries, fighter disciplinary issues, Marshal Courts, or any other issues during this reporting period.  Please include specifics such as individuals involved, event where the incident occurred, etc.";
		}
		HTML para1 = new HTML(p1);
		para1.setStylePrimaryName(REPORT_INSTRUCTIONS);
		bk.add(para1);

		final RichTextArea injuries = new RichTextArea();
		injuries.addStyleName(REPORT_TEXT_BOX);
		bk.add(injuries);
		injuries.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				addReportInfo("Injury", injuries.getHTML());
			}
		});


		add(bk);
	}

	@Override
	public void onDisplay() {
		nextButton.setEnabled(true);
	}

	@Override
	public void onLeavePage() {
	}
	
}
