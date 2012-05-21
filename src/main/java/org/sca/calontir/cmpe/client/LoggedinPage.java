/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client;

import com.google.gwt.core.client.EntryPoint;

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
		/*Button close = new Button("Close Window");
		close.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
			}
		});
		RootPanel.get().add(close);
		* 
		*/
	}
}
