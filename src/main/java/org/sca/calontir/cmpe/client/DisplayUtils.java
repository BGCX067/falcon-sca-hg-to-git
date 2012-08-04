package org.sca.calontir.cmpe.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;

/**
 *
 * @author rikscarborough
 */
public class DisplayUtils {

	public static enum Displays {
		SignupForm,
		ListBox,
		FighterForm
	}

	public static void changeDisplay(Displays display) {
		for (Displays d : Displays.values()) {
			if (!d.equals(display)) {
				DOM.getElementById(d.toString()).getStyle().setDisplay(Style.Display.NONE);
			} else {
				DOM.getElementById(display.toString()).getStyle().setDisplay(Style.Display.BLOCK);
			}
		}
	}
}
