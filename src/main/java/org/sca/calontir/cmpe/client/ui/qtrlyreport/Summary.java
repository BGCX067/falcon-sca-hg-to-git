package org.sca.calontir.cmpe.client.ui.qtrlyreport;

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
	public void init() {
		final Panel bk = new FlowPanel();
		String p1 = "Enter a summary of your report for this quarter.";
		HTML para1 = new HTML(p1);
		bk.add(para1);

		final TextArea summary = new TextArea();
		bk.add(summary);


		add(bk);
	}
	
}
