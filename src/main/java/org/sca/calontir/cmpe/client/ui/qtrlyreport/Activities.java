package org.sca.calontir.cmpe.client.ui.qtrlyreport;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import java.util.List;
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

	final private Security security = SecurityFactory.getSecurity();

	@Override
	public void buildPage() {
		final Panel bk = new FlowPanel();
		bk.setStylePrimaryName(REPORTBG);

		if (security.isRole(UserRoles.GROUP_MARSHAL) || security.isRole(UserRoles.KNIGHTS_MARSHAL)) {
			final Panel persInfo = new FlowPanel();
			persInfo.setStylePrimaryName(PERSONAL_INFO);
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

		String p1 = "Enter the activities you have done for this quarter.  Include events you have attended, fighter practices you have been active in, and events you have helped at.";
		HTML para1 = new HTML(p1);
		para1.setStylePrimaryName(REPORT_INSTRUCTIONS);
		bk.add(para1);

		final TextArea activities = new TextArea();
		activities.setStylePrimaryName(REPORT_TEXT_BOX);
		bk.add(activities);
		addRequired("Activities");
		activities.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				addReportInfo("Activities", event.getValue());
			}
		});


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
}
