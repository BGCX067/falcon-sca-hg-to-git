package org.sca.calontir.cmpe.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.History;

/**
 *
 * @author rikscarborough
 */
public class DisplayUtils {

	public static enum Displays {

		SignupForm,
		ListBox,
		FighterForm,
		ReportGen,
		ReportView
	}

	public static void changeDisplay(Displays display, boolean history) {
		if (history) {
			History.newItem("display:" + display.toString());
		}
		changeDisplay(display);
	}

	public static void changeDisplay(Displays display) {
		for (Displays d : Displays.values()) {
			if (!d.equals(display)) {
				Element element = DOM.getElementById(d.toString());
				if (element != null) {
					element.getStyle().setDisplay(Style.Display.NONE);
				}
			} else {
				Element element = DOM.getElementById(display.toString());
				if (element != null) {
					element.getStyle().setDisplay(Style.Display.BLOCK);
				}
			}
		}
	}

	public static void resetDisplay() {
		DOM.getElementById("SearchBar").getStyle().setDisplay(Style.Display.BLOCK);
		changeDisplay(Displays.SignupForm);
	}

	public static void clearDisplay() {
		DOM.getElementById("SearchBar").getStyle().setDisplay(Style.Display.NONE);
		for (Displays d : Displays.values()) {
			Element element = DOM.getElementById(d.toString());
			if (element != null) {
				element.getStyle().setDisplay(Style.Display.NONE);
			}
		}
		if(DOM.getElementById(Displays.ReportGen.toString()) != null) {
			DOM.getElementById(Displays.ReportGen.toString()).removeFromParent();
		}
		if(DOM.getElementById(Displays.ReportView.toString()) != null) {
			DOM.getElementById(Displays.ReportView.toString()).removeFromParent();
		}

	}
}
