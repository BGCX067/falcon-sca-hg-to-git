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
		String p1 = "Press submit to send your report.  When you submit your report, you should recieve a copy of your report by email.";
		HTML para1 = new HTML(p1);
		bk.add(para1);
		
		add(bk);
	}
	
}
