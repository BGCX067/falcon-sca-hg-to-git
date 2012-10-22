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
public class InjuryReport extends BaseReportPage {

	@Override
	public void buildPage() {
		final Panel bk = new FlowPanel();
		bk.setStylePrimaryName(REPORTBG);

		String p1 = "Enter the problems or injuries that have happened during this quarter.";
		HTML para1 = new HTML(p1);
		para1.setStylePrimaryName(REPORT_INSTRUCTIONS);
		bk.add(para1);

		final TextArea injuries = new TextArea();
		injuries.setStylePrimaryName(REPORT_TEXT_BOX);
		bk.add(injuries);
		injuries.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				addReportInfo("Injury", event.getValue());
			}
		});


		add(bk);
	}
	
}
