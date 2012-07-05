/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 *
 * @author rikscarborough
 */
public class LogoutPage implements EntryPoint {

	public native void closeBrowser() /*-{ $wnd.close(); }-*/;

	public native void reloadParent() /*-{ $wnd.opener.location.reload(); }-*/;

	public native void parentToBye() /*-{ $wnd.opener.location = '/goodbye.jsp' }-*/;
	protected static final String SIGN_OUT_TEXT = "This will log you out of your Google Account.";
	private String logoutUrl;

	@Override
	public void onModuleLoad() {
		try {
			final Panel tilePanel = RootPanel.get("tile");
			logoutUrl = Window.Location.getParameter("logoutUrl");
			final Panel p1 = new FlowPanel();
			p1.add(loadClear());

			final Panel p2 = new FlowPanel();
			p2.add(loadLogout());

			tilePanel.add(p1);
			tilePanel.add(p2);
		} catch (Exception e) {
			Window.alert(e.getMessage());
		}
	}

	private Anchor loadClear() {
		Anchor clearLink = new Anchor("Sign out of Falcon");
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
		Anchor signOutLink = new Anchor("Sign Out of Google");
		signOutLink.setHref(logoutUrl);
		signOutLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				clearLocalData();
			}
		});
		//signOutLink.setStyleName(CALONBARLINK);
		signOutLink.setTitle(SIGN_OUT_TEXT);
		signOutLink.setText(SIGN_OUT_TEXT);
		return signOutLink;
	}
}
