package org.sca.calontir.cmpe.client.ui.qtrlyreport;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.sca.calontir.cmpe.client.FighterInfo;
import org.sca.calontir.cmpe.client.FighterListResultWrapper;
import org.sca.calontir.cmpe.client.FighterService;
import org.sca.calontir.cmpe.client.FighterServiceAsync;
import org.sca.calontir.cmpe.client.ui.LookupController;
import org.sca.calontir.cmpe.client.ui.Shout;
import org.sca.calontir.cmpe.client.user.Security;
import org.sca.calontir.cmpe.client.user.SecurityFactory;
import org.sca.calontir.cmpe.dto.ScaGroup;

/**
 *
 * @author rikscarborough
 */
public class FighterComment extends BaseReportPage {

    private static final Logger log = Logger.getLogger(FighterComment.class.getName());
    final private Security security = SecurityFactory.getSecurity();
    private final Shout shout = Shout.getInstance();
    private String cursor = null;
    private int prevStart = 0;

    @Override
    public void buildPage() {
        final Panel bk = new FlowPanel();
        bk.setStylePrimaryName(REPORTBG);

        String p1 = "Below is the list of Active fighters for your group.  Please provide any comments you have on any of the fighters in your group, such as if they have not fought in six months.";
        HTML para1 = new HTML(p1);
        para1.setStylePrimaryName(REPORT_INSTRUCTIONS);
        bk.add(para1);

        final RichTextArea fighterComments = new RichTextArea();
        fighterComments.addStyleName(REPORT_TEXT_BOX);
        bk.add(fighterComments);
        fighterComments.addBlurHandler(new BlurHandler() {

            @Override
            public void onBlur(BlurEvent event) {
                addReportInfo("Fighter Comments", fighterComments.getHTML());
            }
        });

        fighterComments.addKeyPressHandler(new RequiredFieldKeyPressHandler("Fighter Comments"));

        addListPanel(bk);

        add(bk);
    }

    private void addListPanel(Panel target) {
        Panel listPanel = new FlowPanel();

        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        SimplePager pager = new SimplePager(SimplePager.TextLocation.CENTER, pagerResources, false, 0, true);
        CellTable<FighterInfo> table = new CellTable<FighterInfo>();
        pager.setDisplay(table);

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
        authorizationColumn.setSortable(false);

        TextColumn<FighterInfo> statusColumn = new TextColumn<FighterInfo>() {
            @Override
            public String getValue(FighterInfo fli) {
                return fli.getStatus();
            }
        };
        statusColumn.setSortable(true);

        table.addColumn(scaNameColumn, "SCA Name");
        table.addColumn(authorizationColumn, "Authorizations");
        table.addColumn(statusColumn, "Status");

        ScaGroup group = LookupController.getInstance().getScaGroup(security.getLoginInfo().getGroup());
        AsyncDataProvider<FighterInfo> dataProvider = new AsyncDataProviderImpl(table, group);
        dataProvider.addDataDisplay(table);

        listPanel.add(table);
        listPanel.add(pager);

        target.add(listPanel);

    }

    @Override
    public void onDisplay() {
        nextButton.setEnabled(true);
    }

    @Override
    public void onLeavePage() {
    }

    private class AsyncDataProviderImpl extends AsyncDataProvider<FighterInfo> {

        CellTable<FighterInfo> table = new CellTable<FighterInfo>();
        private final ScaGroup group;

        public AsyncDataProviderImpl(CellTable<FighterInfo> table, ScaGroup group) {
            this.table = table;
            this.group = group;
        }

        @Override
        protected void onRangeChanged(final HasData<FighterInfo> display) {
            shout.hide();
            shout.tell("Please Wait, searching for records....", false);
            int dispStart = display.getVisibleRange().getStart();
            int dispLength = display.getVisibleRange().getLength();

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
            fighterService.getFightersByGroup(group, cursor, dispLength, prevPageStart, new AsyncCallback<FighterListResultWrapper>() {

                @Override
                public void onFailure(Throwable caught) {
                    shout.hide();
                    log.log(Level.SEVERE, "loadgroup:", caught);
                    shout.defaultError();
                }

                @Override
                public void onSuccess(FighterListResultWrapper result) {
                    final Range range = display.getVisibleRange();
                    int start = range.getStart();
                    prevStart = start;
                    table.setRowCount(result.getCount());
                    table.setRowData(start, result.getFighters().getFighterInfo());
                    cursor = result.getCursor();
                    shout.hide();
                }
            });
        }

    }
}
