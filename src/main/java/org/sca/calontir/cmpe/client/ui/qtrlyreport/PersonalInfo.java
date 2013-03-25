/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.ui.qtrlyreport;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import java.util.logging.Logger;
import org.sca.calontir.cmpe.client.FighterService;
import org.sca.calontir.cmpe.client.FighterServiceAsync;
import org.sca.calontir.cmpe.client.IndexPage;
import org.sca.calontir.cmpe.client.ui.Shout;
import org.sca.calontir.cmpe.client.ui.fighterform.FighterForm;
import org.sca.calontir.cmpe.client.user.Security;
import org.sca.calontir.cmpe.client.user.SecurityFactory;
import org.sca.calontir.cmpe.dto.Fighter;

/**
 *
 * @author rikscarborough
 */
public class PersonalInfo extends BaseReportPage {

	private static final Logger log = Logger.getLogger(IndexPage.class.getName());
	final private Security security = SecurityFactory.getSecurity();
	final private Panel infoPanel = new FlowPanel();
	private Fighter user;

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
				String msg = "getFighter: " + caught.getMessage();
				log.severe(msg);
			}

			@Override
			public void onSuccess(Fighter result) {
				user = result;
				bk.add(buildInfoPanel(false));
			}
		});

		add(bk);
	}

	private Panel buildInfoPanel(final boolean edit) {
		infoPanel.clear();

		infoPanel.setStylePrimaryName("dataBox");

		Panel dataHeader = new FlowPanel();
		dataHeader.setStyleName("dataHeader");
		dataHeader.add(new InlineLabel("Personal Info"));
		Panel buttonPanel = new FlowPanel();
		buttonPanel.setStyleName("editButton");
		buttonPanel.getElement().getStyle().setDisplay(Style.Display.INLINE);
		if (edit) {
			buttonPanel.add(cancelButton());
			buttonPanel.add(saveButton());
		} else {
			buttonPanel.add(editButton());
		}
		dataHeader.add(buttonPanel);

		Panel dataBody = new FlowPanel();
		dataBody.setStyleName("dataBody");
		dataBody.add(new FighterForm(user, edit, false));


		infoPanel.add(dataHeader);
		infoPanel.add(dataBody);

		return infoPanel;
	}

	private Widget editButton() {
		final Anchor editButton = new Anchor("edit");
		editButton.addStyleName("buttonLink");
		editButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				editButton.setEnabled(false);
				buildInfoPanel(true);
			}
		});


		return editButton;
	}

	private Widget cancelButton() {
		final Anchor cancelButton = new Anchor("cancel");
		cancelButton.addStyleName("buttonLink");
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				cancelButton.setEnabled(false);
				buildInfoPanel(false);
			}
		});


		return cancelButton;
	}

	private Widget saveButton() {
		final Anchor saveButton = new Anchor("save");
		saveButton.addStyleName("buttonLink");
		saveButton.getElement().getStyle().setMarginRight(1.5, Style.Unit.EM);
		saveButton.addClickHandler(new ClickHandler() {
			private boolean unclicked = true;

			@Override
			public void onClick(ClickEvent event) {
				if (unclicked) {
					unclicked = false;
					saveButton.setEnabled(false);
					FighterServiceAsync fighterService = GWT.create(FighterService.class);

					fighterService.saveFighter(user, new AsyncCallback<Long>() {
						@Override
						public void onFailure(Throwable caught) {
							String msg = "getFighter: " + caught.getMessage();
							log.severe(msg);
						}

						@Override
						public void onSuccess(Long result) {
							log.info("Success");
							validate();
						}
					});
					buildInfoPanel(false);
				}
			}
		});
		return saveButton;
	}

	private boolean validate() {
		boolean retvalue = true;
		retvalue = addReportInfo("Membership Expires", user.getMembershipExpires()) && retvalue;
		retvalue = addReportInfo("SCA Name", user.getScaName()) && retvalue;
		retvalue = addReportInfo("Group", user.getScaGroup().getGroupName()) && retvalue;
		retvalue = addReportInfo("Modern Name", user.getModernName()) && retvalue;
		retvalue = addReportInfo("Address", user.getPrimeAddress()) && retvalue;
		retvalue = addReportInfo("SCA Membership No", user.getScaMemberNo()) && retvalue;
		retvalue = addReportInfo("SCA Group", user.getScaGroup().getGroupName()) && retvalue;
		retvalue = addReportInfo("Phone Number", user.getPrimePhone()) && retvalue;
		retvalue = addReportInfo("Email Address", user.getPrimeEmail()) && retvalue;

		if (!retvalue) {
			Shout.getInstance().tell("Please update the require fields on this page.");
		}

		return retvalue;

	}

	@Override
	public void onDisplay() {
		if (validate()) {
			nextButton.setEnabled(true);
		} else {
			nextButton.setEnabled(false);
		}
	}

	@Override
	public void onLeavePage() {
	}
}
