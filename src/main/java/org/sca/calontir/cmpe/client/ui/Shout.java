package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 *
 * @author rikscarborough
 */
public class Shout extends PopupPanel {

    private final HTML comm = new HTML();
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
        if (t != null) {
            t.cancel();
            t = null;
        }
    }

    public void tell(String status) {
        tell(status, true);
    }

    @Override
    public void hide(boolean autoClosed) {
        super.hide(autoClosed);
        comm.setText("");
    }

    public void defaultError() {
        hide();
        tell("Ehue -- We've encountered an error. The error has been logged, and we will take a look. In the meantime please refresh by clicking on \"Home\" or hit F5 on your keyboard.", false);
    }

    public void tell(String status, boolean hide) {
        if (showing) {
            status = comm.getHTML() + "<br>" + status;
        }
        if (t != null) {
            t.cancel();
        }
        comm.setHTML(status);
        showing = true;
        setPopupPositionAndShow(new PopupPanel.PositionCallback() {
            @Override
            public void setPosition(int offsetWidth, int offsetHeight) {
                final int width = Window.getClientWidth();
                final int left = (width - offsetWidth) / 3;
                final int top = 50;
                setPopupPosition(left, top);
            }
        });
        if (hide) {
            t = new Timer() {
                @Override
                public void run() {
                    hide();
                }
            };
            t.schedule(5000);
        }
    }
}
