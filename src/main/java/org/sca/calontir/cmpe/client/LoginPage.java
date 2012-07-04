/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import org.sca.calontir.cmpe.client.user.Security;
import org.sca.calontir.cmpe.client.user.SecurityFactory;

/**
 *
 * @author rikscarborough
 */
public class LoginPage implements EntryPoint {

	public native void closeBrowser() /*-{ $wnd.close(); }-*/; 
	public native void reloadParent() /*-{ $wnd.opener.location.reload(); }-*/;
	protected static final String SIGN_OUT_TEXT = "This will log you out of your Google Account.";
	final private Security security = SecurityFactory.getSecurity();


	@Override
	public void onModuleLoad() {
			
	}

	private Anchor loadLogout() {
		LoginInfo loginInfo = security.getLoginInfo();
		Anchor signOutLink = new Anchor("Sign Out");
		signOutLink.setHref(loginInfo.getLogoutUrl());
		//signOutLink.setStyleName(CALONBARLINK);
		signOutLink.setTitle(SIGN_OUT_TEXT);
		if (loginInfo.getScaName() == null) {
			signOutLink.setText(loginInfo.getNickname());
		} else {
			signOutLink.setText(loginInfo.getScaName());
		}
		return signOutLink;
	}
}
