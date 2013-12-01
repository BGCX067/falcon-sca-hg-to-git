package org.sca.calontir.cmpe.client.ui.qtrlyreport;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import org.sca.calontir.cmpe.common.ReportingMarshalType;

/**
 *
 * @author rikscarborough
 */
public class Final extends BaseReportPage {

    final Panel bk = new FlowPanel();
    Panel report;

    @Override
    public void buildPage() {
        bk.setStylePrimaryName(REPORTBG);

        String p1;
        //if (allRequired()) {
        p1 = "Press submit to send your report.  When you submit your report, you should recieve a copy of your report by email.";
        //} else {
        //p1 = "Some fields which are required for your report have not been filled out.  Please go back and do so.";
        //}
        HTML para1 = new HTML(p1);
        para1.setStylePrimaryName(REPORT_INSTRUCTIONS);
        bk.add(para1);

        add(bk);
    }

    private void buildReport(Panel bk) {
        HeadingElement header = Document.get().createHElement(1);
        header.setInnerText("Marshal Report");
        bk.getElement().insertAfter(header, null);

        String rmType = (String) getReportInfo().get("Reporting Marshal Type");
        ReportingMarshalType rmt = ReportingMarshalType.getByCode(rmType);

        buildOne("Reporting Marshal Type: ", rmt.getValue(), bk);

        if (getReportInfo().containsKey("Marshal Type")) {
            buildOne("Marshal Type: ", getReportInfo().get("Marshal Type").toString(), bk);
        }
        if (getReportInfo().containsKey("Event Name")) {
            buildOne("Event Name: ", getReportInfo().get("Event Name").toString(), bk);
        }
        if (getReportInfo().containsKey("Event Date")) {
            buildOne("Event Date: ", getReportInfo().get("Event Date").toString(), bk);
        }
        if (getReportInfo().containsKey("Report Type")) {
            buildOne("Reporting Period: ", getReportInfo().get("Report Type").toString(), bk);
        }
        if (rmt.equals(ReportingMarshalType.ARMORED_COMBAT) && getReportInfo().containsKey("Marshal Type")) {
            buildOne("Marshal Type: ", getReportInfo().get("Marshal Type").toString(), bk);
        }
        if (getReportInfo().containsKey("SCA Name")) {
            buildOne("SCA Name: ", getReportInfo().get("SCA Name").toString(), bk);
        }
        if (getReportInfo().containsKey("Modern Name")) {
            buildOne("Modern First & Last Name: ", getReportInfo().get("Modern Name").toString(), bk);
        }
        if (getReportInfo().containsKey("Address")) {
            buildOne("Address: ", getReportInfo().get("Address").toString(), bk);
        }
        if (getReportInfo().containsKey("Phone Number")) {
            buildOne("Phone Number: ", getReportInfo().get("Phone Number").toString(), bk);
        }
        if (getReportInfo().containsKey("SCA Membership No")) {
            buildOne("Membership Number: ", getReportInfo().get("SCA Membership No").toString(), bk);
        }
        if (getReportInfo().containsKey("Membership Expires")) {
            buildOne("Membership Expires: ", getReportInfo().get("Membership Expires").toString(), bk);
        }
        if (getReportInfo().containsKey("Group")) {
            buildOne("Home Group: ", getReportInfo().get("Group").toString(), bk);
        }
        if (getReportInfo().containsKey("Active Fighters")) {
            buildOne("Number of Authorized Fighters: ", getReportInfo().get("Active Fighters").toString(), bk);
        }
        if (getReportInfo().containsKey("Minor Fighters")) {
            buildOne("Number of Minors: ", getReportInfo().get("Minor Fighters").toString(), bk);
        }
        if (getReportInfo().containsKey("Activities")) {
            buildTwo("Activities: ", getReportInfo().get("Activities").toString(), bk);
        }
        if (getReportInfo().containsKey("Injury")) {
            buildTwo("Problems or Injuries: ", getReportInfo().get("Injury").toString(), bk);
        }
        if (getReportInfo().containsKey("Fighter Comments")) {
            buildTwo("Fighter Comments: ", getReportInfo().get("Fighter Comments").toString(), bk);
        }
        if (getReportInfo().containsKey("Summary")) {
            buildTwo("Summary: ", getReportInfo().get("Summary").toString(), bk);
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
        if (body != null && !body.trim().isEmpty()) {
            para.setInnerHTML(body);
        }
        bk.getElement().insertAfter(hthree, null);
        bk.getElement().insertAfter(para, null);
    }

    @Override
    public void onDisplay() {
        report = new FlowPanel();
        buildReport(report);
        bk.add(report);
        nextButton.getElement().getStyle().setDisplay(Style.Display.NONE);
        submitButton.getElement().getStyle().setDisplay(Style.Display.INLINE_BLOCK);
    }

    @Override
    public void onLeavePage() {
        bk.remove(report);
        nextButton.getElement().getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        submitButton.getElement().getStyle().setDisplay(Style.Display.NONE);
    }
}
