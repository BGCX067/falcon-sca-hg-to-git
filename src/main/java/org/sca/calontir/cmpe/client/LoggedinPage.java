/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Timer;

/**
 *
 * @author rikscarborough
 */
public class LoggedinPage implements EntryPoint {

	public native void closeBrowser() /*-{ $wnd.close(); }-*/; 
	public native void reloadParent() /*-{ $wnd.opener.location.reload(); }-*/;


	@Override
	public void onModuleLoad() {
		reloadParent();
		final Timer t = new Timer() {
			@Override
			public void run() {
				closeBrowser();
			}
		};

		t.schedule(500);
			
	}
}
