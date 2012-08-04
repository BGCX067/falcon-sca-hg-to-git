package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 *
 * @author rikscarborough
 */
public class Shout extends PopupPanel {

	private Label comm = new Label();

	public Shout() {
		super(true);
		FlowPanel fp = new FlowPanel();
		fp.add(comm);
		setWidget(fp);
	}

	public void tell(String status) {
		comm.setText(status);
		setPopupPositionAndShow(new PopupPanel.PositionCallback() {
			@Override
			public void setPosition(int offsetWidth, int offsetHeight) {
				int width = Window.getClientWidth();
				int height = Window.getClientHeight();
				int left = (width - offsetWidth) / 2;
				int top = 50;
				setPopupPosition(left, top);
				setWidth((offsetWidth - 100) + "px");
			}
		});
		final Timer t = new Timer() {
			@Override
			public void run() {
				hide();
			}
		};
		t.schedule(5000);
	}
}
