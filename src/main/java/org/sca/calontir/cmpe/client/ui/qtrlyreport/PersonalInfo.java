/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.ui.qtrlyreport;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import org.sca.calontir.cmpe.client.FighterService;
import org.sca.calontir.cmpe.client.FighterServiceAsync;
import org.sca.calontir.cmpe.client.user.Security;
import org.sca.calontir.cmpe.client.user.SecurityFactory;
import org.sca.calontir.cmpe.dto.Address;
import org.sca.calontir.cmpe.dto.Fighter;

/**
 *
 * @author rikscarborough
 */
public class PersonalInfo extends BaseReportPage {

	final private Security security = SecurityFactory.getSecurity();

	@Override
	public void buildPage() {
		clear();

		final Panel bk = new FlowPanel();

		String p1 = "Please review this information. If it is not correct, go back to the main page and update your personal information";
		HTML para1 = new HTML(p1);
		bk.add(para1);

		FighterServiceAsync fighterService = GWT.create(FighterService.class);

		fighterService.getFighter(security.getLoginInfo().getFighterId(), new AsyncCallback<Fighter>() {
			@Override
			public void onFailure(Throwable caught) {
				throw new UnsupportedOperationException("Not supported yet.");
			}

			@Override
			public void onSuccess(Fighter result) {
				bk.add(buildTable(result));
			}
		});

		add(bk);
	}

	private FlexTable buildTable(Fighter user) {


		FlexTable table = new FlexTable();
		FlexTable.FlexCellFormatter formatter = table.getFlexCellFormatter();

		table.setStyleName("wide-table");
		table.setText(0, 0, "Modern Name:");
		table.setWidget(0, 1, new Label(user.getModernName()));
		addReportInfo("Modern Name", user.getModernName());
		formatter.setStyleName(0, 0, "label");
		formatter.setStyleName(0, 1, "data");

		final Address address;
		final StringBuilder sb = new StringBuilder();
		if (user.getAddress() != null && !user.getAddress().isEmpty()) {
			address = user.getAddress().get(0);
			sb.append(address.getAddress1());
			if (address.getAddress2() != null && !address.getAddress2().isEmpty()) {
				sb.append(", ");
				sb.append(address.getAddress2());
			}
			sb.append(", ");
			sb.append(address.getCity());
			sb.append(", ");
			sb.append(address.getState());
			sb.append("  ");
			sb.append(address.getPostalCode());
		}
		table.setText(1, 0, "Address:");
		table.setWidget(1, 1, new Label(sb.toString()));
		addReportInfo("Address", sb.toString());
		formatter.setStyleName(1, 0, "label");
		formatter.setStyleName(1, 1, "data");

		table.setText(2, 0, "SCA Membership:");
		table.setWidget(2, 1, new Label(user.getScaMemberNo()));
		addReportInfo("SCA Membership No", user.getScaMemberNo());
		formatter.setStyleName(2, 0, "label");
		formatter.setStyleName(2, 1, "data");

		table.setText(3, 0, "Group:");
		table.setWidget(3, 1, new Label(user.getScaGroup().getGroupName()));
		addReportInfo("SCA Group", user.getScaGroup().getGroupName());
		formatter.setStyleName(3, 0, "label");
		formatter.setStyleName(3, 1, "data");

		table.setWidget(4, 0, new Label("Phone Number:"));
		if (user.getPhone() != null && !user.getPhone().isEmpty()) {
			table.setWidget(4, 1, new Label(user.getPhone().get(0).getPhoneNumber()));
			addReportInfo("Phone Number", user.getPhone().get(0).getPhoneNumber());
		} else {
			table.setWidget(4, 1, new Label(""));
			addReportInfo("Phone Number", "");
		}
		formatter.setStyleName(4, 0, "label");
		formatter.setStyleName(4, 1, "data");

		table.setText(5, 0, "Email Address:");
		if (user.getEmail() != null && !user.getEmail().isEmpty()) {
			String emailAddress = user.getEmail().get(0).getEmailAddress();
			table.setWidget(5, 1, new Label(emailAddress));
			addReportInfo("Email Address", emailAddress);
		} else {
			table.setWidget(5, 1, new Label(""));
			addReportInfo("Email Address", "");
		}
		formatter.setStyleName(5, 0, "label");
		formatter.setStyleName(5, 1, "data");
		
		return table;
	}
}
