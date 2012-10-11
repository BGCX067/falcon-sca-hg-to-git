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
			log.info("Enabled");
			submitButton.setEnabled(true);
		}
	}

	private boolean allRequired() {
		int count = required.size();
		log.info("Count is " + count);
		for(String test : required) {
			if(reportInfo.containsKey(test)) {
				--count;
			}
		}
		log.info("Count is " + count);
		return count == 0;
	}

	public void addRequired(String newRequired) {
		required.add(newRequired);
		if(!allRequired()) {
			log.info("Disabled");
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
