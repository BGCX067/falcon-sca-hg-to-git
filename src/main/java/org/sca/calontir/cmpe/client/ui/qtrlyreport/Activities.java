package org.sca.calontir.cmpe.client.ui.qtrlyreport;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextBox;
import java.util.List;
import java.util.logging.Logger;
import org.sca.calontir.cmpe.client.FighterInfo;
import org.sca.calontir.cmpe.client.FighterService;
import org.sca.calontir.cmpe.client.FighterServiceAsync;
import org.sca.calontir.cmpe.client.ui.LookupController;
import org.sca.calontir.cmpe.client.user.Security;
import org.sca.calontir.cmpe.client.user.SecurityFactory;
import org.sca.calontir.cmpe.common.UserRoles;

/**
 *
 * @author rikscarborough
 */
public class Activities extends BaseReportPage {

	private static final Logger log = Logger.getLogger(Activities.class.getName());
	final private Security security = SecurityFactory.getSecurity();
	final private HTML para1 = new HTML();
	final Panel bk = new FlowPanel();
	final Panel persInfo = new FlowPanel();

	@Override
	public void buildPage() {
		bk.setStylePrimaryName(REPORTBG);

		if (security.isRole(UserRoles.GROUP_MARSHAL) || security.isRole(UserRoles.KNIGHTS_MARSHAL)) {
			persInfo.setStylePrimaryName("activitiesInfo");
			Label authFightersLabel = new Label();
			authFightersLabel.setText("Number of Authorized Fighters: ");
			persInfo.add(authFightersLabel);

			TextBox authFighters = new TextBox();
			authFighters.setReadOnly(true);
			persInfo.add(authFighters);
			updateActiveFighters(authFighters);

			Label minorFightersLabel = new Label();
			minorFightersLabel.setText("Number of Minor Fighters: ");
			persInfo.add(minorFightersLabel);

			TextBox minorFighters = new TextBox();
			minorFighters.setReadOnly(true);
			persInfo.add(minorFighters);
			updateMinorFighters(minorFighters);

			bk.add(persInfo);

		}
		para1.setStylePrimaryName(REPORT_INSTRUCTIONS);
		bk.add(para1);

		final RichTextArea activities = new RichTextArea();
		activities.addStyleName(REPORT_TEXT_BOX);
		bk.add(activities);
		addRequired("Activities");
		activities.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				addReportInfo("Activities", activities.getHTML());
			}
		});
		activities.addKeyPressHandler(requiredFieldKeyPressHandler);

		add(bk);
	}

	private void updateActiveFighters(TextBox target) {
		Integer retVal = 0;
		List<FighterInfo> fighterList = LookupController.getInstance().getFighterList(null);
		FighterInfo userInfo = LookupController.getInstance().getFighter(security.getLoginInfo().getFighterId());
		for (FighterInfo fli : fighterList) {
			if (fli.getGroup().equals(userInfo.getGroup())) {
				++retVal;
			}
		}
		target.setText(retVal.toString());
		addReportInfo("Active Fighters", retVal.toString());
	}

	private void updateMinorFighters(final TextBox target) {
		FighterInfo userInfo = LookupController.getInstance().getFighter(security.getLoginInfo().getFighterId());

		FighterServiceAsync fighterService = GWT.create(FighterService.class);
		fighterService.getMinorTotal(userInfo.getGroup(), new AsyncCallback<Integer>() {
			@Override
			public void onFailure(Throwable caught) {
				throw new UnsupportedOperationException("Not supported yet.");
			}

			@Override
			public void onSuccess(Integer result) {
				target.setText(result.toString());
				addReportInfo("Minor Fighters", result.toString());
			}
		});
	}

	@Override
	public void onDisplay() {
		nextButton.setEnabled(false);
		final String p1;
		String reportType = (String) getReportInfo().get("Report Type");
		log.info("Report type = " + reportType);
		if (reportType.equals("Event")) {
			p1 = "Please describe the activities that took place at this event. Tournaments, pickup fights, melees, and what generally occured.";
			persInfo.getElement().getStyle().setDisplay(Style.Display.NONE);
		} else {
			p1 = "Please describe your activities as a Marshal for this quarter. Include events you have attended in general, fighter practices in which you are active, and events where you may have assisted in Marshalatte activities.";
		}
		para1.setHTML(p1);
	}

	@Override
	public void onLeavePage() {
	}
}
