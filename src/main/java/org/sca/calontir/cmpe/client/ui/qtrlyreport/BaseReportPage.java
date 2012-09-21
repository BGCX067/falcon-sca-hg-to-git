package org.sca.calontir.cmpe.client.ui.qtrlyreport;

import com.google.gwt.user.client.ui.SimplePanel;
import java.util.Map;

/**
 *
 * @author rikscarborough
 */
public abstract class BaseReportPage extends SimplePanel {
	private Map<String, Object> reportInfo;

	public abstract void init();

	public Map<String, Object> getReportInfo() {
		return reportInfo;
	}

	public void setReportInfo(Map<String, Object> reportInfo) {
		this.reportInfo = reportInfo;
	}
}
