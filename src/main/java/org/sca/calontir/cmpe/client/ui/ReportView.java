package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.sca.calontir.cmpe.client.FighterService;
import org.sca.calontir.cmpe.client.FighterServiceAsync;
import org.sca.calontir.cmpe.client.user.Security;
import org.sca.calontir.cmpe.client.user.SecurityFactory;
import org.sca.calontir.cmpe.common.ReportingMarshalType;
import org.sca.calontir.cmpe.common.UserRoles;
import org.sca.calontir.cmpe.dto.Report;

/**
 *
 * @author rikscarborough
 */
public class ReportView extends Composite {

    private static final Logger log = Logger.getLogger(ReportView.class.getName());
    private final FighterServiceAsync fighterService = GWT.create(FighterService.class);
    private final Security security = SecurityFactory.getSecurity();
    private final Panel background = new FlowPanel();
    private final Panel twistyPanel = new FlowPanel();

    public void init() {
        fighterService.getReports(30, new AsyncCallback<List<Report>>() {
            @Override
            public void onFailure(Throwable caught) {
                log.log(Level.SEVERE, "getAllReports{0}", caught);
            }

            @Override
            public void onSuccess(List<Report> result) {
                buildReportPage(result);
            }
        });
        initWidget(background);
        buildDropDown();
    }

    private void buildDropDown() {
        background.add(new InlineLabel("Number of days to report"));
        final ListBox daysDropBox = new ListBox(false);
        daysDropBox.addItem("30 Days", String.valueOf(30));
        daysDropBox.addItem("60 Days", String.valueOf(60));
        daysDropBox.addItem("90 Days", String.valueOf(90));
        daysDropBox.addItem("180 Days", String.valueOf(180));
        daysDropBox.addItem("All", "ALL");
        background.add(daysDropBox);

        daysDropBox.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                String value = daysDropBox.getValue(daysDropBox.getSelectedIndex());
                if (value.equalsIgnoreCase("ALL")) {
                    fighterService.getAllReports(new AsyncCallback<List<Report>>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            log.severe("getAllReports" + caught);
                        }

                        @Override
                        public void onSuccess(List<Report> result) {
                            buildReportPage(result);
                        }
                    });
                } else {

                    int v = Integer.parseInt(value.trim());
                    fighterService.getReports(v, new AsyncCallback<List<Report>>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            log.severe("getReports" + caught);
                        }

                        @Override
                        public void onSuccess(List<Report> result) {
                            buildReportPage(result);
                        }
                    });
                }
            }
        });

        background.add(twistyPanel);
    }

    private void buildReportPage(List<Report> result) {
        twistyPanel.clear();

        for (final Report r : result) {
            final String rmType = (String) r.getReportParams().get("Reporting Marshal Type");
            final ReportingMarshalType rmt = ReportingMarshalType.getByCode(rmType) == null
                    ? ReportingMarshalType.ARMORED_COMBAT : ReportingMarshalType.getByCode(rmType);
            final String header = r.getMarshalName() + " <<>> "
                    + "Report Marshal Type: " + rmt.getValue() + " <<>> "
                    + "Report Type: " + r.getReportType() + " <<>> "
                    + (r.getReportType().toLowerCase().equals("event")
                    ? "Event Name: " + r.getReportParams().get("Event Name")
                    : "Marshal Type: " + r.getReportParams().get("Marshal Type")) + " <<>> "
                    + "Date Entered: " + DateTimeFormat.getMediumDateTimeFormat().format(r.getDateEntered());

            final DisclosurePanel twisty = new DisclosurePanel(header);
            twisty.setStylePrimaryName("reportHeader");
            twisty.getHeader().getElement().getStyle().setBackgroundColor("white");
            Panel content = new FlowPanel();
            Anchor deleteButton = null;
            if (security.isRole(UserRoles.CARD_MARSHAL)) {
                deleteButton = new Anchor("Delete");
                deleteButton.addStyleName("buttonLink");
                deleteButton.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        fighterService.deleteReport(r, new AsyncCallback<Void>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                log.severe("deleteReport " + caught.getMessage());
                            }

                            @Override
                            public void onSuccess(Void result) {
                                background.remove(twisty);
                            }
                        });
                    }
                });
            }
            buildReport(content, r, deleteButton);
            twisty.setContent(content);
            twisty.setAnimationEnabled(true);
            twistyPanel.add(twisty);
        }

    }

    private void buildReport(final Panel bk, final Report report, final Anchor deleteButton) {
        HeadingElement header = Document.get().createHElement(1);
        header.setInnerText("Marshal Report");
        bk.getElement().insertAfter(header, null);

        if (deleteButton != null) {
            bk.add(deleteButton);
        }

        final String rmType = (String) report.getReportParams().get("Reporting Marshal Type");
        final ReportingMarshalType rmt = ReportingMarshalType.getByCode(rmType) == null
                ? ReportingMarshalType.ARMORED_COMBAT : ReportingMarshalType.getByCode(rmType);

        buildOne("Reporting Marshal Type: ", rmt.getValue(), bk);
        buildOne("Reporting Period: ", report.getReportType(), bk);
        buildOne("Marshal Type: ", report.getReportParams().get("Marshal Type"), bk);
        if (report.getReportParams().containsKey("Event Name")) {
            buildOne("Event Name: ", report.getReportParams().get("Event Name"), bk);
        }
        if (report.getReportParams().containsKey("Event Date")) {
            buildOne("Event Date: ", report.getReportParams().get("Event Date"), bk);
        }
        buildOne("SCA Name: ", report.getMarshalName(), bk);
        buildOne("Modern First & Last Name: ", report.getReportParams().get("Modern Name"), bk);
        buildOne("Address: ", report.getReportParams().get("Address"), bk);
        buildOne("Phone Number: ", report.getReportParams().get("Phone Number"), bk);
        buildOne("Membership Number: ", report.getReportParams().get("SCA Membership No"), bk);
        buildOne("Membership Expires: ", report.getReportParams().get("Membership Expires"), bk);
        buildOne("Home Group: ", report.getReportParams().get("Group"), bk);
        if (report.getReportParams().containsKey("Active Fighters")) {
            buildOne("Number of Authorized Fighters: ", report.getReportParams().get("Active Fighters"), bk);
        }
        if (report.getReportParams().containsKey("Minor Fighters")) {
            buildOne("Number of Minors: ", report.getReportParams().get("Minor Fighters"), bk);
        }
        if (report.getReportParams().containsKey("Activities")) {
            buildTwo("Activities: ", report.getReportParams().get("Activities"), bk);
        }
        if (report.getReportParams().containsKey("Injury")) {
            buildTwo("Problems or Injuries: ", report.getReportParams().get("Injury"), bk);
        }
        if (report.getReportParams().containsKey("Fighter Comments")) {
            buildTwo("Fighter Comments: ", report.getReportParams().get("Fighter Comments"), bk);
        }
        if (report.getReportParams().containsKey("Summary")) {
            buildTwo("Summary: ", report.getReportParams().get("Summary"), bk);
        }
    }

    private void buildOne(String title, String body, Panel bk) {

        ParagraphElement para = Document.get().createPElement();
        HeadingElement hthree = Document.get().createHElement(3);
        hthree.getStyle().setDisplay(Style.Display.INLINE);
        hthree.setInnerText(title);
        SpanElement span = Document.get().createSpanElement();
        span.setInnerText(body);
        para.insertFirst(hthree);
        para.insertAfter(span, null);

        bk.getElement().insertAfter(para, null);
    }

    private void buildTwo(String title, String body, Panel bk) {
        HeadingElement hthree = Document.get().createHElement(3);
        hthree.setInnerText(title);
        ParagraphElement para = Document.get().createPElement();
        para.setInnerHTML(body);
        bk.getElement().insertAfter(hthree, null);
        bk.getElement().insertAfter(para, null);
    }
}
