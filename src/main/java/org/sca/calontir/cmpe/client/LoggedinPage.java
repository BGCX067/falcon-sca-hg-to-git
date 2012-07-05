/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;

/**
 *
 * @author rikscarborough
 */
public class LoggedinPage implements EntryPoint {

	public native void closeBrowser() /*-{ $wnd.close(); }-*/;
	public native void reloadParent() /*-{ $wnd.opener.location.reload(); }-*/;
	public native void parentToBye() /*-{ $wnd.opener.location = '/goodbye.jsp' }-*/;

	@Override
	public void onModuleLoad() {
		String target = Window.Location.getParameter("trgt");
		if (target != null && target.equals("goodbye")) {
			parentToBye();
		} else {
			reloadParent();
		}

		final Timer t = new Timer() {
			@Override
			public void run() {
				closeBrowser();
			}
		};

		t.schedule(500);

	}
}
