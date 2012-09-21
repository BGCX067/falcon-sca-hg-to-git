package org.sca.calontir.cmpe.client.ui.qtrlyreport;

import com.google.gwt.user.client.ui.HTML;
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

		String p1 = "Greetings " + security.getLoginInfo().getScaName();
		HTML para1 = new HTML(p1);
		add(para1);
	}
	
}
