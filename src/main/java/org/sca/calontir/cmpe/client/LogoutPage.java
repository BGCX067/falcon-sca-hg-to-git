/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 *
 * @author rikscarborough
 */
public class LogoutPage implements EntryPoint {

	protected static final String LOP_LINK = "loplink";
	private static final String SIGN_OUT_OF__GOOGLE = "Sign out of Google";
	private static final String SIGN_OUT_OF__FALCON = "Sign out of Falcon";
	private static final String PRIVATE_COMPUTER_LABEL = " - Log out of the FALCON application (private computer)";
	public static final String PUBLIC_COMPUTER_LABEL = " - Log out Google account (public or  shared computer)";
	public static final String TITLE = "<h1>Click Log Out option below</h1>";
	public static final String LOPLABEL = "loplabel";

	public native void closeBrowser() /*-{ $wnd.close(); }-*/;

	public native void reloadParent() /*-{ $wnd.opener.location.reload(); }-*/;

	public native void parentToBye() /*-{ $wnd.opener.location = '/goodbye.jsp' }-*/;
	protected static final String SIGN_OUT_TEXT = "This will log you out of your Google Account.";
	private String logoutUrl;

	@Override
	public void onModuleLoad() {
		try {
			final Panel tilePanel = RootPanel.get("tile");
			tilePanel.add(new HTML(TITLE));

			logoutUrl = Window.Location.getParameter("logoutUrl");
			final Panel p1 = new FlowPanel();
			p1.getElement().getStyle().setMarginBottom(1.2, Style.Unit.EM);
			p1.getElement().getStyle().setMarginTop(1.2, Style.Unit.EM);
			p1.add(loadClear());

			final Panel p2 = new FlowPanel();
			p2.getElement().getStyle().setMarginBottom(1.2, Style.Unit.EM);
			p2.getElement().getStyle().setMarginTop(1.2, Style.Unit.EM);
			p2.add(loadLogout());

			Label l1 = new Label(PRIVATE_COMPUTER_LABEL, false);
			l1.setStyleName(LOPLABEL);
			p1.add(l1);
			tilePanel.add(p1);

			Label l2 = new Label(PUBLIC_COMPUTER_LABEL, false);
			l2.setStyleName(LOPLABEL);
			p2.add(l2);
			tilePanel.add(p2);
		} catch (Exception e) {
			Window.alert(e.getMessage());
		}
	}

	private Anchor loadClear() {
		Anchor clearLink = new Anchor(SIGN_OUT_OF__FALCON);
		clearLink.setStyleName(LOP_LINK);
		clearLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				clearLocalData();
				parentToBye();
				closeBrowser();
			}
		});
		return clearLink;
	}

	private void clearLocalData() {
		Storage stockStore = Storage.getLocalStorageIfSupported();
		if (stockStore != null) {
			stockStore.removeItem("scaNameList");
			stockStore.removeItem("scaNameUpdated");
		}
		for (String cookie : Cookies.getCookieNames()) {
			Cookies.removeCookie(cookie, "/");
		}
	}

	private Anchor loadLogout() {
		Anchor signOutLink = new Anchor(SIGN_OUT_OF__GOOGLE);
		signOutLink.setHref(logoutUrl);
		signOutLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				clearLocalData();
			}
		});
		signOutLink.setStyleName(LOP_LINK);
		signOutLink.setTitle(SIGN_OUT_TEXT);
		return signOutLink;
	}
}
