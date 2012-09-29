package org.sca.calontir.cmpe.client.ui.qtrlyreport;

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
	public void init() {
		final Panel bk = new FlowPanel();
		String p1 = "Enter the problems or injuries that have happened during this quarter.";
		HTML para1 = new HTML(p1);
		bk.add(para1);

		final TextArea injuries = new TextArea();
		bk.add(injuries);


		add(bk);
	}
	
}
