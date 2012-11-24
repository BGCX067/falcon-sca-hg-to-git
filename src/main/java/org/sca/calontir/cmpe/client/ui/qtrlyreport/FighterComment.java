package org.sca.calontir.cmpe.client.ui.qtrlyreport;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.view.client.ListDataProvider;
import java.util.List;
import org.sca.calontir.cmpe.client.FighterInfo;
import org.sca.calontir.cmpe.client.ui.LookupController;
import org.sca.calontir.cmpe.client.user.Security;
import org.sca.calontir.cmpe.client.user.SecurityFactory;

/**
 *
 * @author rikscarborough
 */
public class FighterComment extends BaseReportPage {

	final private Security security = SecurityFactory.getSecurity();

	@Override
	public void buildPage() {
		final Panel bk = new FlowPanel();
		bk.setStylePrimaryName(REPORTBG);

		String p1 = "Below is the list of Active fighters for your group.  Please provaide any comments you have on any of the fighters in your group, such as you if they have not fought in six months.";
		HTML para1 = new HTML(p1);
		para1.setStylePrimaryName(REPORT_INSTRUCTIONS);
		bk.add(para1);

		final TextArea fighterComments = new TextArea();
		fighterComments.setStylePrimaryName(REPORT_TEXT_BOX);
		bk.add(fighterComments);
		fighterComments.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				addReportInfo("Fighter Comments", event.getValue());
			}
		});

		List<FighterInfo> fighterList = LookupController.getInstance().getFighterList(null);
		addListPanel(fighterList, bk);

		add(bk);
	}

	private void addListPanel(List<FighterInfo> fighterList, Panel target) {
		Panel listPanel = new FlowPanel();
		listPanel.setStyleName("list");
		FighterInfo userInfo = LookupController.getInstance().getFighter(security.getLoginInfo().getFighterId());

		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		SimplePager pager = new SimplePager(SimplePager.TextLocation.CENTER, pagerResources, false, 0, true);
		CellTable<FighterInfo> table = new CellTable<FighterInfo>();
		pager.setDisplay(table);
		table.addStyleName("header");

		TextColumn<FighterInfo> scaNameColumn = new TextColumn<FighterInfo>() {
			@Override
			public String getValue(FighterInfo fli) {
				return fli.getScaName();
			}
		};
		scaNameColumn.setSortable(false);

		TextColumn<FighterInfo> authorizationColumn = new TextColumn<FighterInfo>() {
			@Override
			public String getValue(FighterInfo fli) {
				return fli.getAuthorizations();
			}
		};
		authorizationColumn.setSortable(false);

//		TextColumn<FighterInfo> minorColumn = new TextColumn<FighterInfo>() {
//			@Override
//			public String getValue(FighterInfo fli) {
//				return fli.getMinor().toString();
//			}
//		};
//		minorColumn.setSortable(false);

		table.addColumn(scaNameColumn, "SCA Name");
		table.addColumn(authorizationColumn, "Authorizations");
		//table.addColumn(minorColumn, "Minor");

		ListDataProvider<FighterInfo> dataProvider = new ListDataProvider<FighterInfo>();
		dataProvider.addDataDisplay(table);

		table.setRowCount(fighterList.size());

		List data = dataProvider.getList();
		data.clear();
		for (FighterInfo fli : fighterList) {
			if (fli.getGroup().equals(userInfo.getGroup())) {
				if (fli.getStatus().equals("ACTIVE")) {
					data.add(fli);
				}
			}
		}

		listPanel.add(table);
		listPanel.add(pager);

		target.add(listPanel);

	}

	@Override
	public void onDisplay() {
		nextButton.setEnabled(true);
	}
}
