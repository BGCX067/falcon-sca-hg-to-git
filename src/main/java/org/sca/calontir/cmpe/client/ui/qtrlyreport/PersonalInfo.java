/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.ui.qtrlyreport;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.datepicker.client.DateBox;
import java.util.Date;
import java.util.logging.Logger;
import org.sca.calontir.cmpe.client.FighterService;
import org.sca.calontir.cmpe.client.FighterServiceAsync;
import org.sca.calontir.cmpe.client.IndexPage;
import org.sca.calontir.cmpe.client.ui.Shout;
import org.sca.calontir.cmpe.client.user.Security;
import org.sca.calontir.cmpe.client.user.SecurityFactory;
import org.sca.calontir.cmpe.dto.Address;
import org.sca.calontir.cmpe.dto.Fighter;

/**
 *
 * @author rikscarborough
 */
public class PersonalInfo extends BaseReportPage {
	private static final Logger log = Logger.getLogger(IndexPage.class.getName());
	final private Security security = SecurityFactory.getSecurity();

	@Override
	public void buildPage() {
		clear();

		final Panel bk = new FlowPanel();
		bk.setStylePrimaryName(REPORTBG);

		String p1 = "Please review this information. If it is not correct, go back to the main page and update your personal information";
		HTML para1 = new HTML(p1);
		para1.addStyleName(REPORT_INSTRUCTIONS);
		bk.add(para1);

		FighterServiceAsync fighterService = GWT.create(FighterService.class);

		fighterService.getFighter(security.getLoginInfo().getFighterId(), new AsyncCallback<Fighter>() {
			@Override
			public void onFailure(Throwable caught) {
				log.severe("getFighter: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Fighter result) {
				bk.add(buildTable(result));
			}
		});

		add(bk);
	}

	private FlexTable buildTable(final Fighter user) {


		FlexTable table = new FlexTable();
		FlexTable.FlexCellFormatter formatter = table.getFlexCellFormatter();

		table.setStyleName("wide-table");

		addReportInfo("SCA Name", user.getScaName());

		table.setText(0, 0, "Group:");
		table.setWidget(0, 1, new Label(user.getScaGroup().getGroupName()));
		addReportInfo("Group", user.getScaGroup().getGroupName());
		formatter.setStyleName(0, 0, "label");
		formatter.setStyleName(0, 1, "data");

		table.setText(1, 0, "Modern Name:");
		table.setWidget(1, 1, new Label(user.getModernName()));
		addReportInfo("Modern Name", user.getModernName());
		formatter.setStyleName(1, 0, "label");
		formatter.setStyleName(1, 1, "data");

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
		table.setText(2, 0, "Address:");
		table.setWidget(2, 1, new Label(sb.toString()));
		addReportInfo("Address", sb.toString());
		formatter.setStyleName(2, 0, "label");
		formatter.setStyleName(2, 1, "data");

		table.setText(3, 0, "SCA Membership:");
		table.setWidget(3, 1, new Label(user.getScaMemberNo()));
		addReportInfo("SCA Membership No", user.getScaMemberNo());
		formatter.setStyleName(3, 0, "label");
		formatter.setStyleName(3, 1, "data");

		final DateBox membershipExpires = new DateBox();
		membershipExpires.getTextBox().getElement().setId("membershipExpires");
		membershipExpires.getTextBox().setName("membershipExpires");
		membershipExpires.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("MM/dd/yyyy")));
		membershipExpires.setStyleName("membershipExpires");
		if (user.getMembershipExpires() != null && !user.getMembershipExpires().isEmpty()) {
			membershipExpires.setValue(DateTimeFormat.getFormat("MM/dd/yyyy").parse(user.getMembershipExpires()));
		}
		addRequired("Membership Expires");
		addReportInfo("Membership Expires", user.getMembershipExpires());
		membershipExpires.getTextBox().addKeyPressHandler(requiredFieldKeyPressHandler);
		membershipExpires.addValueChangeHandler(new ValueChangeHandler<Date>() {
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				//if (membershipExpires.getTextBox().getValue().matches("\\d{2}/\\d{2}/\\d{4}")) {
					addReportInfo("Membership Expires", membershipExpires.getTextBox().getValue());
				//} else {
					//Shout.getInstance().tell("Date must be entered as MM/dd/yyyy");
				//}
			}
		});

		table.setText(4, 0, "Membership Expiration Date:");
		table.setWidget(4, 1, membershipExpires);
		formatter.setStyleName(4, 0, "label");
		formatter.setStyleName(4, 1, "data");

		table.setText(5, 0, "Group:");
		table.setWidget(5, 1, new Label(user.getScaGroup().getGroupName()));
		addReportInfo("SCA Group", user.getScaGroup().getGroupName());
		formatter.setStyleName(5, 0, "label");
		formatter.setStyleName(5, 1, "data");

		table.setWidget(6, 0, new Label("Phone Number:"));
		if (user.getPhone() != null && !user.getPhone().isEmpty()) {
			table.setWidget(6, 1, new Label(user.getPhone().get(0).getPhoneNumber()));
			addReportInfo("Phone Number", user.getPhone().get(0).getPhoneNumber());
		} else {
			table.setWidget(6, 1, new Label(""));
			addReportInfo("Phone Number", "");
		}
		formatter.setStyleName(6, 0, "label");
		formatter.setStyleName(6, 1, "data");

		table.setText(7, 0, "Email Address:");
		if (user.getEmail() != null && !user.getEmail().isEmpty()) {
			String emailAddress = user.getEmail().get(0).getEmailAddress();
			table.setWidget(7, 1, new Label(emailAddress));
			addReportInfo("Email Address", emailAddress);
		} else {
			table.setWidget(7, 1, new Label(""));
			addReportInfo("Email Address", "");
		}
		formatter.setStyleName(7, 0, "label");
		formatter.setStyleName(7, 1, "data");

		return table;
	}

	@Override
	public void onDisplay() {
		if(getReportInfo().keySet().contains("Membership Expires")) {
			nextButton.setEnabled(true);
		} else {
			nextButton.setEnabled(false);
		}
	}

	@Override
	public void onLeavePage() {
	}
}
