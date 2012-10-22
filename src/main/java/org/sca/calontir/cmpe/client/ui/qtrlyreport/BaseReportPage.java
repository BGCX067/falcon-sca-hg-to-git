package org.sca.calontir.cmpe.client.ui.qtrlyreport;

import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author rikscarborough
 */
public abstract class BaseReportPage extends SimplePanel {
	private static final Logger log = Logger.getLogger(BaseReportPage.class.getName());
	private Map<String, Object> reportInfo;
	private List<String> required;
	private FocusWidget submitButton;

	public static final String REPORT_BUTTON_PANEL = "reportButtonPanel";
	public static final String REPORTBG = "reportbg";
	public static final String REPORT_TITLE = "reportTitle";
	public static final String REPORT_INSTRUCTIONS = "reportInstructions";
	public static final String REPORT_TEXT_BOX = "reportTextBox";
	public static final String PERSONAL_INFO = "personalInfo";

	public void init(Map<String, Object> reportInfo, List<String> required, FocusWidget submitButton) {
		this.reportInfo = reportInfo;
		this.required = required;
		this.submitButton = submitButton;
		buildPage();
	}

	public abstract void buildPage();

	public void addReportInfo(String key, Object value) {
		reportInfo.put(key, value);
		if(allRequired()) {
			submitButton.setEnabled(true);
		}
	}

	private boolean allRequired() {
		int count = required.size();
		for(String test : required) {
			if(reportInfo.containsKey(test)) {
				--count;
			}
		}
		return count == 0;
	}

	public void addRequired(String newRequired) {
		required.add(newRequired);
		if(!allRequired()) {
			submitButton.setEnabled(false);
		}
	}

	public Map<String, Object> getReportInfo() {
		return reportInfo;
	}

	public List<String> getRequired() {
		return required;
	}
}
