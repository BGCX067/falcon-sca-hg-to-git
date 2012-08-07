package org.sca.calontir.cmpe.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 *
 * @author rikscarborough
 */
public class GoodbyePage implements EntryPoint {

	@Override
	public void onModuleLoad() {
		try {
			Panel tilePanel = RootPanel.get("tile");
			Image titleImage = new Image();
			titleImage.setUrl("images/title_image.gif");
			titleImage.addStyleName("titleImage");
			tilePanel.add(titleImage);

			Label titleLabel = new Label("FALCON");
			titleLabel.setWordWrap(false);
			titleLabel.setTitle("Fighter Authorization List Calontir ONline (FALCON)");
			titleLabel.setStyleName("title");

			HTML p = new HTML();
			p.setText("Thank you for using the FALCON Card Marshal application.");


			tilePanel.add(titleLabel);
			tilePanel.add(p);
		} catch (Exception e) {
		}
	}
}
