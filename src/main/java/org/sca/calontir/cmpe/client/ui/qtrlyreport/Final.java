package org.sca.calontir.cmpe.client.ui.qtrlyreport;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;

/**
 *
 * @author rikscarborough
 */
public class Final extends BaseReportPage {

	@Override
	public void buildPage() {
		final Panel bk = new FlowPanel();
		String p1 = "Thank you for your report.  You should recieve a copy in your email shortly.  Press Home to get back to the falcon application";
		HTML para1 = new HTML(p1);
		bk.add(para1);
	}
	
}
