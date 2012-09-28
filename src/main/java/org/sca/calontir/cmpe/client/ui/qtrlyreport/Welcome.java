package org.sca.calontir.cmpe.client.ui.qtrlyreport;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import org.sca.calontir.cmpe.client.user.Security;
import org.sca.calontir.cmpe.client.user.SecurityFactory;

/**
 *
 * @author rikscarborough
 */
public class Welcome extends BaseReportPage {
	final private Security security = SecurityFactory.getSecurity();

	@Override
	public void init() {
		clear();

		Panel bk = new FlowPanel();
		String p1 = "<h1>Marshal Report Form</h1>";
		HTML para1 = new HTML(p1);
		para1.setStyleName("reportTitle");
		bk.add(para1);

		String p2 = "To complete, press the Next>> button then following the instructions.";
		HTML para2 = new HTML(p2);
		para2.setStyleName("reportBody");
		bk.add(para2);

		add(bk);
	}
	
}
