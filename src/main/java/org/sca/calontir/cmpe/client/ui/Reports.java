package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.sca.calontir.cmpe.client.DisplayUtils;
import static org.sca.calontir.cmpe.client.ui.CalonBar.CALONBARLINK;
import org.sca.calontir.cmpe.client.user.Security;
import org.sca.calontir.cmpe.client.user.SecurityFactory;

/**
 *
 * @author rikscarborough
 */
public class Reports extends Composite {

    private static final Logger log = Logger.getLogger(Reports.class.getName());
    private final Security security = SecurityFactory.getSecurity();
    private final Panel background = new FlowPanel();

    private final Anchor reportViewLink = new Anchor("Marshal Report View");
    private final Anchor earlMarshalReportLink = new Anchor("Earl Marshal Report");

    public void init() {
        initWidget(background);

        reportViewLink.setStyleName(CALONBARLINK);
        reportViewLink.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                GWT.runAsync(new RunAsyncCallback() {
                    @Override
                    public void onFailure(Throwable reason) {
                        log.log(Level.SEVERE, reason.getMessage(), reason);
                    }

                    @Override
                    public void onSuccess() {
                        DisplayUtils.clearDisplay();
                        ReportView reportView = new ReportView();
                        reportView.init();
                        reportView.getElement().setId(DisplayUtils.Displays.ReportView.toString());

                        Panel tilePanel = RootPanel.get("tile");
                        tilePanel.add(reportView);
                    }
                });
            }
        });
        background.add(reportViewLink);

        background.add(getDivBar());

        earlMarshalReportLink.setHref("/EarlMarshalReport");
        earlMarshalReportLink.setStyleName(CALONBARLINK);
        earlMarshalReportLink.setTarget("reports");
        background.add(earlMarshalReportLink);
    }

    private Label getDivBar() {
        Label divBar = new Label();
        divBar.setText(" | ");
        divBar.setStyleName(CALONBARLINK);
        return divBar;
    }
}
