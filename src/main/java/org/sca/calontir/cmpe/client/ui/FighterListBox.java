/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ImageCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.sca.calontir.cmpe.client.DisplayUtils;
import org.sca.calontir.cmpe.client.FighterInfo;
import org.sca.calontir.cmpe.client.FighterListResultWrapper;
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

    private static final Logger log = Logger.getLogger(FighterListBox.class.getName());
    final private ListDataProvider<FighterInfo> dataProvider = new ListDataProvider<>();
    final private CellTable<FighterInfo> table = new CellTable<>();
    final private Security security = SecurityFactory.getSecurity();
    private String cursor = null;
    private int prevStart = 0;

    public FighterListBox() {
        Panel listBackground = new FlowPanel();
        listBackground.getElement().setId(DisplayUtils.Displays.ListBox.toString());
        listBackground.getElement().getStyle().setDisplay(Style.Display.NONE);

        Panel listPanel = new FlowPanel();
        listPanel.addStyleName("top");
        listPanel.addStyleName("inline_table");

        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        SimplePager pager = new SimplePager(SimplePager.TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(table);

        ButtonCell selectButton = new ButtonCell();
        Column<FighterInfo, String> selectColumn = new Column<FighterInfo, String>(selectButton) {
            @Override
            public String getValue(FighterInfo fighter) {
                if (security.canView(fighter)) {
                    return "Select";
                } else {
                    return "...";
                }
            }
        };
        selectColumn.setSortable(false);
        selectColumn.setFieldUpdater(new FieldUpdater<FighterInfo, String>() {
            @Override
            public void update(int index, FighterInfo fighter, String value) {
                if (!security.canView(fighter)) {
                    Shout.getInstance().tell("You do not have rights to update this record");
                }
            }
        });

        ImageCell imageCell = new ImageCell();
        final Image legendImage = new Image();
        legendImage.setUrl("/images/falcon_icon_legend.png");

        Column<FighterInfo, String> imageColumn = new Column<FighterInfo, String>(imageCell) {
            @Override
            public String getValue(FighterInfo fighter) {
                if (fighter.getRole().equals(UserRoles.USER.toString())) {
                    return "/images/authorizedFighter.png";
                } else if (fighter.getRole().equals(UserRoles.MARSHAL_OF_THE_FIELD.toString())) {
                    return "/images/warrantedMarshal.png";
                } else if (fighter.getRole().equals(UserRoles.CUT_AND_THRUST_MARSHAL.toString())) {
                    return "/images/falconCutAndThrustMarshal.png";
                } else if (fighter.getRole().equals(UserRoles.KNIGHTS_MARSHAL.toString())) {
                    return "/images/knightsMarshal.png";
                } else if (fighter.getRole().equals(UserRoles.DEPUTY_EARL_MARSHAL.toString())) {
                    return "/images/regionalDeputy.png";
                } else if (fighter.getRole().equals(UserRoles.CARD_MARSHAL.toString())) {
                    return "/images/warrantedMarshal.png";
                } else if (fighter.getRole().equals(UserRoles.EARL_MARSHAL.toString())) {
                    return "/images/earlMarshal.png";
                }
                return "/images/authorizedFighter.png";
            }
        };
        imageColumn.setSortable(false);

        TextColumn<FighterInfo> scaNameColumn = new TextColumn<FighterInfo>() {
            @Override
            public String getValue(FighterInfo fli) {
                return fli.getScaName();
            }
        };
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
        table.addColumn(imageColumn);
        table.addColumn(scaNameColumn, "SCA Name");
        ColumnSortEvent.ListHandler<FighterInfo> columnSortHandler = new ColumnSortEvent.ListHandler<>(dataProvider.getList());
        columnSortHandler.setComparator(scaNameColumn, new Comparator<FighterInfo>() {
            @Override
            public int compare(FighterInfo left, FighterInfo right) {
                return left.getScaName().compareTo(right.getScaName());
            }
        });
        table.addColumnSortHandler(columnSortHandler);
        table.addColumn(authorizationColumn, "Authorizations");
        table.addColumn(groupColumn, "Group");
        columnSortHandler = new ColumnSortEvent.ListHandler<>(dataProvider.getList());
        columnSortHandler.setComparator(groupColumn, new Comparator<FighterInfo>() {
            @Override
            public int compare(FighterInfo left, FighterInfo right) {
                return left.getGroup().compareTo(right.getGroup());
            }
        });
        table.addColumnSortHandler(columnSortHandler);

        if (security.isRoleOrGreater(UserRoles.KNIGHTS_MARSHAL)) {
            TextColumn<FighterInfo> statusColumn = new TextColumn<FighterInfo>() {
                @Override
                public String getValue(FighterInfo fli) {
                    return fli.getStatus();
                }
            };
            statusColumn.setSortable(true);

            table.addColumn(statusColumn, "Status");
            columnSortHandler = new ColumnSortEvent.ListHandler<>(dataProvider.getList());
            columnSortHandler.setComparator(statusColumn, new Comparator<FighterInfo>() {
                @Override
                public int compare(FighterInfo left, FighterInfo right) {
                    return left.getStatus().compareTo(right.getStatus());
                }
            });
            table.addColumnSortHandler(columnSortHandler);
        }

        final SingleSelectionModel<FighterInfo> selectionModel = new SingleSelectionModel<>();
        table.setSelectionModel(selectionModel);
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                FighterInfo selected = selectionModel.getSelectedObject();
                if (selected != null) {
                    if (security.canView(selected)) {
                        FighterServiceAsync fighterService = GWT.create(FighterService.class);

                        fighterService.getFighter(selected.getFighterId(), new AsyncCallback<Fighter>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                log.log(Level.SEVERE, "getFighter {0}", caught);
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
        Panel legendPanel = new FlowPanel();
        legendPanel.addStyleName("inline");
        legendPanel.getElement().getStyle().setMargin(2.5, Style.Unit.EM);
        legendPanel.add(legendImage);
        listBackground.add(legendPanel);

        initWidget(listBackground);

    }

    @Override
    public void find(String searchName) {
        log.info("Searching for " + searchName);

        LookupController.getInstance().searchFighters(searchName, table, dataProvider);
        DisplayUtils.changeDisplay(DisplayUtils.Displays.ListBox, true);
    }

    @Override
    public void loadAll() {
        AsyncDataProvider<FighterInfo> aDataProvider = new AsyncDataProviderImpl();
        aDataProvider.addDataDisplay(table);

        //table.setVisibleRangeAndClearData(new Range(0, 10), true);
        DisplayUtils.changeDisplay(DisplayUtils.Displays.ListBox, true);
    }

    @Override
    public void loadGroup(ScaGroup group) {
        AsyncDataProvider<FighterInfo> aDataProvider = new AsyncDataProviderImpl(group);
        aDataProvider.addDataDisplay(table);

        //table.setVisibleRangeAndClearData(new Range(0, 10), true);
        DisplayUtils.changeDisplay(DisplayUtils.Displays.ListBox, true);
    }

    @Override
    public void switchSearchType(SearchType searchType) {
    }

    protected enum DataProviderType {

        FIGHTER, GROUP
    }

    private class AsyncDataProviderImpl extends AsyncDataProvider<FighterInfo> {

        private DataProviderType dataProviderType;
        private ScaGroup group;

        public AsyncDataProviderImpl() {
            this.dataProviderType = DataProviderType.FIGHTER;
            this.group = null;
        }

        public AsyncDataProviderImpl(ScaGroup group) {
            this.group = group;
            this.dataProviderType = DataProviderType.GROUP;

        }

        @Override
        protected void onRangeChanged(final HasData<FighterInfo> display) {
            int dispStart = display.getVisibleRange().getStart();
            int dispLength = display.getVisibleRange().getLength();
            log.info("display start: " + dispStart + " length " + dispLength);

            int prevPageStart = dispStart - dispLength;
            prevPageStart = prevPageStart < 0 ? 0 : dispStart;
            if (prevStart >= dispStart) {
                cursor = null;
            }
            if (dispStart >= table.getRowCount() - dispLength) {
                cursor = null;
                prevPageStart = table.getRowCount() - dispLength;
            }
            final FighterServiceAsync fighterService = GWT.create(FighterService.class);
            switch (dataProviderType) {
                case FIGHTER:
                    fighterService.getFighters(cursor, dispLength, prevPageStart, new fighterAsyncCallback(display));
                    break;
                case GROUP:
                    fighterService.getFightersByGroup(group, cursor, dispLength, prevPageStart, new fighterAsyncCallback(display));
                    break;
                default:
            }
        }

        private class fighterAsyncCallback implements AsyncCallback<FighterListResultWrapper> {

            private final HasData<FighterInfo> display;

            public fighterAsyncCallback(HasData<FighterInfo> display) {
                this.display = display;
            }

            @Override
            public void onFailure(Throwable caught) {
                log.log(Level.SEVERE, "loadAll:", caught);
            }

            @Override
            public void onSuccess(FighterListResultWrapper result) {
                final Range range = display.getVisibleRange();
                int start = range.getStart();
                prevStart = start;
                table.setRowCount(result.getCount());
                table.setRowData(start, result.getFighters().getFighterInfo());
                cursor = result.getCursor();
            }
        }
    }
}
