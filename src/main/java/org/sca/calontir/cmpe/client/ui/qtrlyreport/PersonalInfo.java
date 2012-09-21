/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.ui.qtrlyreport;

import com.google.gwt.user.client.ui.HTML;
import java.util.Map;
import org.sca.calontir.cmpe.client.user.Security;
import org.sca.calontir.cmpe.client.user.SecurityFactory;

/**
 *
 * @author rikscarborough
 */
public class PersonalInfo extends BaseReportPage {
	

	final private Security security = SecurityFactory.getSecurity();

	public void init() {
		clear();

		String p1 = "Please review this information. If it is not correct, go back to the main page and update your personal information";
		HTML para1 = new HTML(p1);
		add(para1);
	}

}
