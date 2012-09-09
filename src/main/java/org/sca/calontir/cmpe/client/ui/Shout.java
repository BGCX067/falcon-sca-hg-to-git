package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 *
 * @author rikscarborough
 */
public class Shout extends PopupPanel {

	private HTML comm = new HTML();
	private final static Shout _instance = new Shout();
	private boolean showing = false;
	private Timer t = null;

	private Shout() {
		super(true);
		FlowPanel fp = new FlowPanel();
		comm.setWordWrap(true);
		fp.add(comm);
		setWidget(fp);
	}

	public static Shout getInstance() {
		return _instance;
	}

	@Override
	public void hide() {
		showing = false;
		super.hide();
		comm.setText("");
		if(t != null) {
			t.cancel();
			t = null;
		}
	}
	

	public void tell(String status) {
		if(showing) {
			status = comm.getHTML() + "<br>" + status;
		}
		if(t != null) {
			t.cancel();
		}
		comm.setHTML(status);
		showing = true;
		setPopupPositionAndShow(new PopupPanel.PositionCallback() {
			@Override
			public void setPosition(int offsetWidth, int offsetHeight) {
				int width = Window.getClientWidth();
				int height = Window.getClientHeight();
				int left = (width - offsetWidth) / 2;
				int top = 50;
				setPopupPosition(left, top);
				//setWidth((offsetWidth - 100) + "px");
			}
		});
		t = new Timer() {
			@Override
			public void run() {
				hide();
			}
		};
		t.schedule(5000);
	}
}
