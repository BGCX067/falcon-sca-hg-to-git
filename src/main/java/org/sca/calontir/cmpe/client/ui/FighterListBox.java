/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import java.util.Comparator;
import java.util.List;
import org.sca.calontir.cmpe.client.DisplayUtils;
import org.sca.calontir.cmpe.client.FighterInfo;
import org.sca.calontir.cmpe.client.FighterService;
import org.sca.calontir.cmpe.client.FighterServiceAsync;
import org.sca.calontir.cmpe.client.ui.SearchEvent.SearchType;
import org.sca.calontir.cmpe.client.user.Security;
import org.sca.calontir.cmpe.client.user.SecurityFactory;
import org.sca.calontir.cmpe.common.UserRoles;
import org.sca.calontir.cmpe.dto.Fighter;
import org.sca.calontir.cmpe.dto.ScaGroup;

/**
 *
 * @author rikscarborough
 */
public class FighterListBox extends Composite implements SearchEventHandler {

	final private ListDataProvider<FighterInfo> dataProvider = new ListDataProvider<FighterInfo>();
	final private CellTable<FighterInfo> table = new CellTable<FighterInfo>();
	final private Security security = SecurityFactory.getSecurity();

	public FighterListBox() {
		Panel listBackground = new FlowPanel();
		listBackground.getElement().setId(DisplayUtils.Displays.ListBox.toString());
		listBackground.getElement().getStyle().setDisplay(Style.Display.NONE);

		Panel listPanel = new FlowPanel();

		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		SimplePager pager = new SimplePager(SimplePager.TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(table);

		ButtonCell selectButton = new ButtonCell();
		Column<FighterInfo, String> selectColumn = new Column<FighterInfo, String>(selectButton) {
			@Override
			public String getValue(FighterInfo fighter) {
				if (security.canView(fighter.getFighterId())) {
					return "Select";
				} else {
					return "     ";
				}
			}
		};
		selectColumn.setSortable(false);

		TextColumn<FighterInfo> scaNameColumn = new TextColumn<FighterInfo>() {
			@Override
			public String getValue(FighterInfo fli) {
				return fli.getScaName();
			}
		};
		//TODO: Turning off sorting for now. Once everything else is settled,
		//      revisit this.
		scaNameColumn.setSortable(true);

		TextColumn<FighterInfo> authorizationColumn = new TextColumn<FighterInfo>() {
			@Override
			public String getValue(FighterInfo fli) {
				return fli.getAuthorizations();
			}
		};

		TextColumn<FighterInfo> groupColumn = new TextColumn<FighterInfo>() {
			@Override
			public String getValue(FighterInfo fli) {
				return fli.getGroup();
			}
		};
		groupColumn.setSortable(true);

		table.addColumn(selectColumn, "");
		table.addColumn(scaNameColumn, "SCA Name");
		ColumnSortEvent.ListHandler<FighterInfo> columnSortHandler = new ColumnSortEvent.ListHandler<FighterInfo>(dataProvider.getList());
		columnSortHandler.setComparator(scaNameColumn, new Comparator<FighterInfo>() {
			@Override
			public int compare(FighterInfo left, FighterInfo right) {
				return left.getScaName().compareTo(right.getScaName());
			}
		});
		table.addColumnSortHandler(columnSortHandler);
		table.addColumn(authorizationColumn, "Authorizations");
		table.addColumn(groupColumn, "Group");
		columnSortHandler = new ColumnSortEvent.ListHandler<FighterInfo>(dataProvider.getList());
		columnSortHandler.setComparator(groupColumn, new Comparator<FighterInfo>() {
			@Override
			public int compare(FighterInfo left, FighterInfo right) {
				return left.getGroup().compareTo(right.getGroup());
			}
		});
		table.addColumnSortHandler(columnSortHandler);

		if(security.isRoleOrGreater(UserRoles.GROUP_MARSHAL)) {
			TextColumn<FighterInfo> statusColumn = new TextColumn<FighterInfo>() {
				@Override
				public String getValue(FighterInfo fli) {
					return fli.getStatus();
				}
			};
			statusColumn.setSortable(false);

			table.addColumn(statusColumn, "Status");
		}

		final SingleSelectionModel<FighterInfo> selectionModel = new SingleSelectionModel<FighterInfo>();
		table.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				FighterInfo selected = selectionModel.getSelectedObject();
				if (selected != null) {
					if (security.canView(selected.getFighterId())) {
						FighterServiceAsync fighterService = GWT.create(FighterService.class);

						fighterService.getFighter(selected.getFighterId(), new AsyncCallback<Fighter>() {
							@Override
							public void onFailure(Throwable caught) {
								throw new UnsupportedOperationException("Not supported yet.");
							}

							@Override
							public void onSuccess(Fighter result) {
								fireEvent(new EditViewEvent(Mode.VIEW, result));
							}
						});
					}
				}
			}
		});


		dataProvider.addDataDisplay(table);



		listPanel.add(table);
		listPanel.add(pager);

		listBackground.add(listPanel);
		initWidget(listBackground);

	}

	@Override
	public void find(String searchName) {
		List<FighterInfo> fighterList = LookupController.getInstance().getFighterList(searchName);
		table.setRowCount(fighterList.size());
		List data = dataProvider.getList();
		data.clear();
		for (FighterInfo fli : fighterList) {
			data.add(fli);
		}

		DisplayUtils.changeDisplay(DisplayUtils.Displays.ListBox, true);
	}

	@Override
	public void loadAll() {
		List<FighterInfo> fighterList = LookupController.getInstance().getFighterList(null);
		table.setRowCount(fighterList.size());
		List data = dataProvider.getList();
		data.clear();
		for (FighterInfo fli : fighterList) {
			data.add(fli);
		}

		DisplayUtils.changeDisplay(DisplayUtils.Displays.ListBox, true);
	}

	@Override
	public void loadGroup(ScaGroup group) {
		List<FighterInfo> fighterList = LookupController.getInstance().getFighterList(null);
		table.setRowCount(fighterList.size());
		List data = dataProvider.getList();
		data.clear();
		for (FighterInfo fli : fighterList) {
			if (fli.getGroup().equals(group.getGroupName())) {
				data.add(fli);
			}
		}

		DisplayUtils.changeDisplay(DisplayUtils.Displays.ListBox, true);
	}

	@Override
	public void switchSearchType(SearchType searchType) {
	}
}
