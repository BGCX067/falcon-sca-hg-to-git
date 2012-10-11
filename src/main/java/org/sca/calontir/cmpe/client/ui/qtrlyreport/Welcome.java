package org.sca.calontir.cmpe.client.ui.qtrlyreport;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import org.sca.calontir.cmpe.client.user.Security;
import org.sca.calontir.cmpe.client.user.SecurityFactory;
import org.sca.calontir.cmpe.common.UserRoles;

/**
 *
 * @author rikscarborough
 */
public class Welcome extends BaseReportPage {
	final private Security security = SecurityFactory.getSecurity();

	@Override
	public void buildPage() {
		clear();

		Panel bk = new FlowPanel();
		String p1 = "<h1>Marshal Report Form</h1>";
		HTML para1 = new HTML(p1);
		para1.setStyleName("reportTitle");
		bk.add(para1);

		String p2;
		if(security.isRole(UserRoles.DEPUTY_EARL_MARSHAL)) {
			p2 = "To complete Deputy Earl Marshal Report, press the Next>> button then follow the instructions.";
		} else if(security.isRole(UserRoles.GROUP_MARSHAL) || security.isRole(UserRoles.KNIGHTS_MARSHAL)) {
			p2 = "To complete Group Marshal Report, press the Next>> button then follow the instructions.";
		} else {
			p2 = "To complete Marshal of the Field Report, press the Next>> button then follow the instructions.";
		}
		HTML para2 = new HTML(p2);
		para2.setStyleName("reportBody");
		bk.add(para2);

		add(bk);
	}
	
}
