package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextArea;
import org.sca.calontir.cmpe.client.ui.qtrlyreport.BaseReportPage;

/**
 *
 * @author rikscarborough
 */
public class Activities extends BaseReportPage {

	@Override
	public void init() {
		final Panel bk = new FlowPanel();

		String p1 = "Enter the activities you have done for this quarter.  Include events you have attended, fighter practices you have been active in, and events you have helped at.";
		HTML para1 = new HTML(p1);
		bk.add(para1);

		final TextArea activities = new TextArea();
		bk.add(activities);


		add(bk);
	}
	
}
