/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.dom.client.Style;
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

	private static class AboutPanel extends PopupPanel {

		public AboutPanel() {
			super(true);
			FlowPanel fp = new FlowPanel();
			fp.add(new HTML("The <strong>Fighter Authorization List Calontir Online (FALCON)</strong> system was designed to better manage Calontir’s fighter card information, issuance and tracking process."));
			fp.add(new Label("The system allows the Calontir Marshallate to keep and update records in a centralized system as well as allowing Calontir’s fighters to maintain their own point-of-contact information, and print their own fighter cards at home."));
			fp.add(new HTML("&nbsp;"));
			fp.add(new Label("The system requires one of these preferred HTML5 capable browsers:"));
			fp.add(new HTML("<a href=\"https://www.google.com/intl/en/chrome/\">Google Chrome</a> 18.x.xxx.xxx (or better)"));
			fp.add(new HTML("<a href=\"http://www.mozilla.org/en-US/firefox/new/\">Firefox 12.0</a>  (or better)"));
			fp.add(new HTML("<a href=\"http://windows.microsoft.com/en-US/internet-explorer/products/ie/home\">Windows Internet Explorer 8</a> (or better)"));
			fp.add(new HTML("You also need <a href=\"http://get.adobe.com/reader/\">Adobe Reader</a>"));
			fp.add(new HTML("&nbsp;"));
			fp.add(new HTML("We'd love to hear your feedback on the system. Click the <a href=\"https://docs.google.com/spreadsheet/viewform?formkey=dExnMU0tMDE2UWZyVDY3TE1Ic3lfRHc6MQ#gid=0\">Feedback</a> link on the page header."));
			fp.add(new HTML("&nbsp;"));
			fp.add(new Label("This system has been brought to you by:"));
			fp.add(new HTML("<ol>"));
			fp.add(new HTML("<li>His Grace Martino Michel Venneri, Earl Marshal of Calontir."));
			fp.add(new HTML("<li>Sir Gustav Jameson, Project lead and Mastermind."));
			fp.add(new HTML("<li>His Lordship Brendan Mac an tSaoir, Lead Programer."));
			fp.add(new HTML("<li>Taiji Bataciqan-nu Ko'un Ashir, Current Card Marshal and Alpha Tester."));
			fp.add(new HTML("<li>Sir Duncan Bruce of Logan, Programming Consultant."));
			fp.add(new HTML("<li>Sir Hans Krieger, Programming Consultant."));
			fp.add(new HTML("<li>Her Ladyship Kalisa Martel, Marshalatte Consultant."));
			fp.add(new HTML("<li>His Lordship Aiden O'Seaghdma, Graphic Arts and Design of the fighter card imagery."));
			fp.add(new HTML("<li>Mistress Olga Belobashnia Cherepanova, Contributing artist.  Provider of the Falcon logo."));
			fp.add(new HTML("<li>The CSS Stylin for the page is based on the design create by Her Ladyship Sung Sai-êrh for the Calontir website."));
			fp.add(new HTML("</ol>"));
			setWidget(fp);
		}
	}

	public CalonBar() {

		DOM.setElementAttribute(barPanel.getElement(), "id", CALONBAR);
//
//		Label titleLabel = new Label("Falcon");
//		titleLabel.setWordWrap(false);
//		titleLabel.setTitle("Fighter Authorization List Calontir Online (FALCON)");
//		titleLabel.setStyleName("title");
//
//		barPanel.add(titleLabel);

		final FlowPanel linkbarPanel = new FlowPanel();
		linkbarPanel.setStyleName("linkbar");

		homeLink.setHref(INDEXHTML);
		homeLink.setStyleName(CALONBARLINK);
		homeLink.setTitle("Click here to return to the home page");
//		homeLink.addClickHandler(new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//			}
//		});
		linkbarPanel.add(homeLink);

		linkbarPanel.add(getDivBar());

		loginPanel.setStyleName(CALONBARLINK);
		linkbarPanel.add(loginPanel);

		Security security = SecurityFactory.getSecurity();
		loginInfo = security.getLoginInfo();
		if (loginInfo.isLoggedIn()) {
			Label nameLabel = new Label();
			if (loginInfo.getScaName() == null) {
				nameLabel.setText(loginInfo.getNickname());
			} else {
				nameLabel.setText(loginInfo.getScaName());
			}
			nameLabel.setStyleName(CALONBARLINK);
			nameLabel.getElement().getStyle().setDisplay(Style.Display.INLINE);
			linkbarPanel.add(nameLabel);
		} else {
			loadLogin();
		}

		linkbarPanel.add(getDivBar());

		feedBackLink.setHref(FEEDBACKLINK);
		feedBackLink.setStyleName(CALONBARLINK);
		linkbarPanel.add(feedBackLink);

		linkbarPanel.add(getDivBar());

		//aboutLink.setHref(ABOUT_PAGE);
		aboutLink.setStyleName(CALONBARLINK);
		aboutLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final AboutPanel ab = new AboutPanel();
				ab.setTitle("Click outside the About box to close.");
				ab.setPopupPositionAndShow(new PopupPanel.PositionCallback() {

					@Override
					public void setPosition(int offsetWidth, int offsetHeight) {
						int left = 50; //(Window.getClientWidth() - offsetWidth);
						int top = 50; //(Window.getClientHeight() - offsetHeight) - 144;
						ab.setPopupPosition(left, top);
						ab.setWidth((offsetWidth - 100) + "px");
						ab.addStyleName("aboutbox");
					}
				});

			}
		});

		linkbarPanel.add(aboutLink);

		linkbarPanel.add(getDivBar());

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
		linkbarPanel.add(syncLink);

		barPanel.add(linkbarPanel);

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
		signInLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Window.open("", "loginwindow", "width=940,height=400,status=1,resizeable=1,scrollbars=1");
			}
		});
		signInLink.setTarget("loginwindow");
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
