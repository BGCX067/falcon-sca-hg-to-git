package org.sca.calontir.cmpe.client.ui.qtrlyreport;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextArea;

/**
 *
 * @author rikscarborough
 */
public class Summary extends BaseReportPage {

	@Override
	public void buildPage() {
		final Panel bk = new FlowPanel();
		String p1 = "Enter a summary of your report for this quarter.";
		HTML para1 = new HTML(p1);
		bk.add(para1);

		final TextArea summary = new TextArea();
		bk.add(summary);
		addRequired("Summary");
		summary.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				addReportInfo("Summary", event.getValue());
			}
		});


		add(bk);
	}
	
}
