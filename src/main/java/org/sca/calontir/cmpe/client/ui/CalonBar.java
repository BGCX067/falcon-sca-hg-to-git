package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.sca.calontir.cmpe.client.LoginInfo;
import org.sca.calontir.cmpe.client.user.Security;
import org.sca.calontir.cmpe.client.user.SecurityFactory;

/**
 * This widget places a navigation bar on the client.
 *
 * @author rikscarborough
 */
public class CalonBar extends Composite {

	protected static final String CALONBAR = "calonbar";
	protected static final String INDEXHTML = "/";
	protected static final String LOGOUTPAGE = "logout.jsp";
	protected static final String CALONBARLINK = "calonbarlink";
	protected static final String FEEDBACKLINK = "https://docs.google.com/spreadsheet/viewform?formkey=dExnMU0tMDE2UWZyVDY3TE1Ic3lfRHc6MQ#gid=0";
	protected static final String SUPPORTLINK = "https://sites.google.com/site/calontirmmproject/support";
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
	private Anchor supportLink = new Anchor("Support");
//	private Anchor syncLink = new Anchor("Sync");

	private static class AboutPanel extends PopupPanel {

		public AboutPanel() {
			super(true);
			FlowPanel fp = new FlowPanel();
			FlowPanel base = new FlowPanel();
			base.setStyleName("base");
			fp.add(base);
			FlowPanel tile = new FlowPanel();
			tile.setStyleName("tile");
			base.add(tile);
			tile.add(new HTML("The <strong>Fighter Authorization List Calontir Online (FALCON)</strong> system was designed to better manage Calontir’s fighter card information, issuance and tracking process."));
			tile.add(new Label("The system allows the Calontir Marshallate to keep and update records in a centralized system as well as allowing Calontir’s fighters to maintain their own point-of-contact information, and print their own fighter cards at home."));
			tile.add(new HTML("&nbsp;"));
			tile.add(new Label("The system requires one of these preferred HTML5 capable browsers:"));
			tile.add(new HTML("<a href=\"https://www.google.com/intl/en/chrome/\">Google Chrome</a> 18.x.xxx.xxx (or better)"));
			tile.add(new HTML("<a href=\"http://www.mozilla.org/en-US/firefox/new/\">Firefox 12.0</a>  (or better)"));
			tile.add(new HTML("<a href=\"http://windows.microsoft.com/en-US/internet-explorer/products/ie/home\">Windows Internet Explorer 8</a> (or better)"));
			tile.add(new HTML("You also need <a href=\"http://get.adobe.com/reader/\">Adobe Reader</a>"));
			tile.add(new HTML("&nbsp;"));
			tile.add(new HTML("We'd love to hear your feedback on the system. Click the <a href=\"https://docs.google.com/spreadsheet/viewform?formkey=dExnMU0tMDE2UWZyVDY3TE1Ic3lfRHc6MQ#gid=0\">Feedback</a> link on the page header."));
			tile.add(new HTML("&nbsp;"));
			tile.add(new Label("This system has been brought to you by:"));
			tile.add(new HTML("<ol>"));
			tile.add(new HTML("<li>His Grace Martino Michel Venneri, Earl Marshal of Calontir."));
			tile.add(new HTML("<li>Sir Gustav Jameson, Project lead."));
			tile.add(new HTML("<li>His Lordship Brendan Mac an tSaoir, Lead Programer."));
			tile.add(new HTML("<li>Taiji Bataciqan-nu Ko'un Ashir, Current Card Marshal and Alpha Tester."));
			tile.add(new HTML("<li>Sir Duncan Bruce of Logan, Programming Consultant."));
			tile.add(new HTML("<li>Sir Hans Krieger, Programming Consultant."));
			tile.add(new HTML("<li>Her Ladyship Kalisa Martel, Marshalatte Consultant."));
			tile.add(new HTML("<li>His Lordship Aiden O'Seaghdma, Graphic Arts and Design of the fighter card imagery."));
			tile.add(new HTML("<li>His Lordship Mathieu Chartrain, Programer"));
			tile.add(new HTML("<li>Mistress Olga Belobashnia Cherepanova, Contributing artist.  Provider of the Falcon logo."));
			tile.add(new HTML("<li>The CSS Styling for the page is based on the design created by Her Ladyship Sung Sai-êrh for the Calontir website."));
			tile.add(new HTML("</ol>"));
			Label versionLine = new Label("falcon version 1.0.5");
			versionLine.getElement().getStyle().setFontStyle(Style.FontStyle.ITALIC);
			versionLine.getElement().getStyle().setFontSize(85.0, Style.Unit.PCT);
			versionLine.getElement().getStyle().setProperty("textAlign", "right");
			tile.add(versionLine);
			setWidget(fp);
		}
	}

	public CalonBar() {

		DOM.setElementAttribute(barPanel.getElement(), "id", CALONBAR);

		final FlowPanel linkbarPanel = new FlowPanel();
		linkbarPanel.setStyleName("linkbar");

		homeLink.setHref(INDEXHTML);
		homeLink.setStyleName(CALONBARLINK);
		homeLink.setTitle("Click here to return to the home page");
		linkbarPanel.add(homeLink);

		linkbarPanel.add(getDivBar());

		loginPanel.setStyleName(CALONBARLINK);
		linkbarPanel.add(loginPanel);

		Security security = SecurityFactory.getSecurity();
		loginInfo = security.getLoginInfo();
		if (loginInfo.isLoggedIn()) {
			loadLogout();
		} else {
			loadLogin();
		}

		linkbarPanel.add(getDivBar());

		feedBackLink.setHref(FEEDBACKLINK);
		feedBackLink.setStyleName(CALONBARLINK);
		linkbarPanel.add(feedBackLink);

		linkbarPanel.add(getDivBar());

		supportLink.setHref(SUPPORTLINK);
		supportLink.setStyleName(CALONBARLINK);
		linkbarPanel.add(supportLink);

		linkbarPanel.add(getDivBar());

		aboutLink.setStyleName(CALONBARLINK);
		aboutLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final AboutPanel ab = new AboutPanel();
				ab.setTitle("Click outside the About box to close.");
				ab.setPopupPositionAndShow(new PopupPanel.PositionCallback() {

					@Override
					public void setPosition(int offsetWidth, int offsetHeight) {
						int left = 50;
						int top = 50;
						ab.setPopupPosition(left, top);
						ab.setWidth((offsetWidth - 100) + "px");
						ab.addStyleName("aboutbox");
					}
				});

			}
		});

		linkbarPanel.add(aboutLink);

//		linkbarPanel.add(getDivBar());

//		syncLink.addClickHandler(new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//				Storage stockStore = Storage.getLocalStorageIfSupported();
//				if (stockStore != null) {
//					stockStore.removeItem("scaNameList");
//					stockStore.removeItem("scaNameUpdated");
//					Window.Location.assign("/");
//				}
//			}
//		});
//		syncLink.setStyleName(CALONBARLINK);
//		linkbarPanel.add(syncLink);

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
		signOutLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String gLogoutUrl = loginInfo.getLogoutUrl();
				Window.open(GWT.getHostPageBaseURL() + LOGOUTPAGE + "?logoutUrl=" + gLogoutUrl, "loginwindow", "width=940,height=400,location=no,status=1,resizeable=1,scrollbars=1");
			}
		});
		signOutLink.setTarget("loginwindow");
		signOutLink.setStyleName(CALONBARLINK);
		signOutLink.setTitle(SIGN_OUT_TEXT);
		loginPanel.add(signOutLink);
	}
}
