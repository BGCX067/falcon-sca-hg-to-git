package org.sca.calontir.cmpe.client.ui.qtrlyreport;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;

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

		buildOne("Reporting Period: ", (String)getReportInfo().get("Report Type"), bk);
		buildOne("Marshal Type: ", (String)getReportInfo().get("Marshal Type"), bk);
		buildOne("SCA Name: ", (String)getReportInfo().get("SCA Name"), bk);
		buildOne("Modern First & Last Name: ", (String)getReportInfo().get("Modern Name"), bk);
		buildOne("Address: ", (String)getReportInfo().get("Address"), bk);
		buildOne("Phone Number: ", (String)getReportInfo().get("Phone Number"), bk);
		buildOne("Membership Number: ", (String)getReportInfo().get("SCA Membership No"), bk);
		buildOne("Membership Expires: ", (String)getReportInfo().get("Membership Expires"), bk);
		buildOne("Home Group: ", (String)getReportInfo().get("Group"), bk);
		if (getReportInfo().containsKey("Active Fighters")) {
			buildOne("Number of Authorized Fighters: ", (String)getReportInfo().get("Active Fighters"), bk);
		}
		if (getReportInfo().containsKey("Minor Fighters")) {
			buildOne("Number of Minors: ", (String)getReportInfo().get("Minor Fighters"), bk);
		}
		if (getReportInfo().containsKey("Activities")) {
			buildTwo("Activities: ", (String)getReportInfo().get("Activities"), bk);
		}
		if (getReportInfo().containsKey("Injury")) {
			buildTwo("Problems or Injuries: ", (String)getReportInfo().get("Injury"), bk);
		}
		if (getReportInfo().containsKey("Fighter Comments")) {
			buildTwo("Fighter Comments: ", (String)getReportInfo().get("Fighter Comments"), bk);
		}
		if (getReportInfo().containsKey("Summary")) {
			buildTwo("Summary: ", (String)getReportInfo().get("Summary"), bk);
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
