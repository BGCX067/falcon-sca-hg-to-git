/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.sca.calontir.cmpe.client.LoginInfo;
import org.sca.calontir.cmpe.client.user.Security;
import org.sca.calontir.cmpe.client.user.SecurityFactory;

/**
 *
 * @author rikscarborough
 */
public class CalonBar extends Composite {

	protected static final String CALONBAR = "calonbar";
	protected static final String INDEXHTML = "/";
	protected static final String ABOUT_PAGE = "/about.jsp";
	protected static final String CALONBARLINK = "calonbarlink";
	protected static final String FEEDBACKLINK = "https://docs.google.com/spreadsheet/viewform?formkey=dExnMU0tMDE2UWZyVDY3TE1Ic3lfRHc6MQ#gid=0";
	protected static final String SIGN_IN_TEXT = "Please sign in to your Google Account.";
	protected static final String SIGN_OUT_TEXT = "This will log you out of your Google Account.";
	private Panel barPanel = new FlowPanel();
	private Panel loginPanel = new FlowPanel();
	private LoginInfo loginInfo = null;
	private Anchor homeLink = new Anchor("Home");
	private Anchor aboutLink = new Anchor("About");
	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");
	private Anchor feedBackLink = new Anchor("Feedback");
	private Anchor syncLink = new Anchor("Sync");

	public CalonBar() {

		DOM.setElementAttribute(barPanel.getElement(), "id", CALONBAR);


		final FlowPanel titlePanel = new FlowPanel();
		DOM.setElementAttribute(titlePanel.getElement(), "id", "title");
		titlePanel.setStyleName("title");

		Label titleLabel = new Label("Falcon");
		titleLabel.setWordWrap(false);
		titleLabel.setTitle("Fighter Authorization List Calontir Online (FALCON)");

		barPanel.add(titleLabel);

		homeLink.setHref(INDEXHTML);
		homeLink.setStyleName(CALONBARLINK);
//		homeLink.addClickHandler(new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//			}
//		});
		barPanel.add(homeLink);

		barPanel.add(getDivBar());

		loginPanel.setStyleName(CALONBARLINK);
		barPanel.add(loginPanel);

		Security security = SecurityFactory.getSecurity();
		loginInfo = security.getLoginInfo();
		if (loginInfo.isLoggedIn()) {
			loadLogout();
		} else {
			loadLogin();
		}

		barPanel.add(getDivBar());

		feedBackLink.setHref(FEEDBACKLINK);
		feedBackLink.setStyleName(CALONBARLINK);
		barPanel.add(feedBackLink);

		barPanel.add(getDivBar());

		aboutLink.setHref(ABOUT_PAGE);
		aboutLink.setStyleName(CALONBARLINK);
		barPanel.add(aboutLink);

		barPanel.add(getDivBar());

		syncLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Storage stockStore = Storage.getLocalStorageIfSupported();
				if (stockStore != null) {
					stockStore.removeItem("scaNameList");
					stockStore.removeItem("scaNameUpdated");
					Window.Location.assign("/");
				}
			}
		});
		syncLink.setStyleName(CALONBARLINK);
		barPanel.add(syncLink);

		initWidget(barPanel);
	}

	private Label getDivBar() {
		Label divBar = new Label();
		divBar.setText(" | ");
		divBar.setStyleName(CALONBARLINK);
		return divBar;
	}

	private void loadLogin() {
		signInLink.setHref(loginInfo.getLoginUrl());
		signInLink.setStyleName(CALONBARLINK);
		signInLink.setTitle(SIGN_IN_TEXT);
		loginPanel.add(signInLink);
	}

	private void loadLogout() {
		signOutLink.setHref(loginInfo.getLogoutUrl());
		signOutLink.setStyleName(CALONBARLINK);
		signOutLink.setTitle(SIGN_OUT_TEXT);
		if (loginInfo.getScaName() == null) {
			signOutLink.setText(loginInfo.getNickname());
		} else {
			signOutLink.setText(loginInfo.getScaName());
		}
		loginPanel.add(signOutLink);
	}
}
