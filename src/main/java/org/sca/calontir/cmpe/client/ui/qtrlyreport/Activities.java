package org.sca.calontir.cmpe.client.ui.qtrlyreport;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextArea;
import org.sca.calontir.cmpe.client.ui.qtrlyreport.BaseReportPage;
import org.sca.calontir.cmpe.client.user.Security;
import org.sca.calontir.cmpe.client.user.SecurityFactory;
import org.sca.calontir.cmpe.common.UserRoles;

/**
 *
 * @author rikscarborough
 */
public class Activities extends BaseReportPage {

	final private Security security = SecurityFactory.getSecurity();

	@Override
	public void init() {
		final Panel bk = new FlowPanel();

//		if (security.isRole(UserRoles.GROUP_MARSHAL) || security.isRole(UserRoles.KNIGHTS_MARSHAL)) {
//			Label authFighters = new Label();
//			authFighters.setText("Number of Authorized Fighters: ");
//			bk.add(authFighters);
//			Label minorFighters = new Label();
//			minorFighters.setText("Number of Minor Fighters: ");
//			bk.add(authFighters);
//		}

		String p1 = "Enter the activities you have done for this quarter.  Include events you have attended, fighter practices you have been active in, and events you have helped at.";
		HTML para1 = new HTML(p1);
		bk.add(para1);

		final TextArea activities = new TextArea();
		bk.add(activities);


		add(bk);
	}
}
