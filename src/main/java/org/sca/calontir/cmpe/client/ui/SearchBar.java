/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import java.util.List;
import java.util.logging.Logger;
import org.sca.calontir.cmpe.client.FighterInfo;
import org.sca.calontir.cmpe.client.ui.SearchEvent.SearchType;
import org.sca.calontir.cmpe.client.user.Security;
import org.sca.calontir.cmpe.client.user.SecurityFactory;
import org.sca.calontir.cmpe.common.UserRoles;
import org.sca.calontir.cmpe.dto.Authorization;
import org.sca.calontir.cmpe.dto.Fighter;
import org.sca.calontir.cmpe.dto.ScaGroup;

/**
 *
 * @author rikscarborough
 */
public class SearchBar extends Composite implements DataUpdatedEventHandler, SearchEventHandler {

    private static final Logger logger = Logger.getLogger(SearchBar.class.getName());
    private static final String SEARCH = "Search";
    final private Security security = SecurityFactory.getSecurity();
    final FlowPanel searchPanel = new FlowPanel();
    private SearchBox searchBox;
    private ListBox groupBox;
    private Button submit;

    public SearchBar() {
        searchPanel.getElement().setAttribute("id", "searchBar");
        buildBar();
        initWidget(searchPanel);
    }

    private void buildBar() {
        searchBox = new SearchBox();

        groupBox = buildGroupBox();
        groupBox.getElement().setAttribute("id", "groupBox");
        groupBox.getElement().getStyle().setDisplay(Style.Display.NONE);
        searchPanel.add(searchBox);
        searchPanel.add(groupBox);

        submit = new Button(SEARCH, new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Shout.getInstance().tell("Please Wait, searching for records....", false);
                final String searchName = searchBox.getText();
                logger.info("Searching for " + searchName);
                if (searchName == null || searchName.isEmpty()) {
                    logger.info("lookup all");
                    fireEvent(new SearchEvent());
                } else {
                    logger.info("Searching for name");
                    fireEvent(new SearchEvent(searchName));
                }
            }
        });
        searchPanel.add(submit);

        if (security.isRoleOrGreater(UserRoles.CARD_MARSHAL)) {
            Button add = new Button("Add", new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    fireEvent(new EditViewEvent(Mode.ADD));
                }
            });

            searchPanel.add(add);
        }

        RadioButton fighterButton = new RadioButton("searchGroup", "fighter");
        fighterButton.setValue(Boolean.TRUE, false);
        fighterButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                fireEvent(new SearchEvent(SearchEvent.SearchType.FIGHTER));
            }
        });
        RadioButton groupButton = new RadioButton("searchGroup", "group");
        groupButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                fireEvent(new SearchEvent(SearchEvent.SearchType.GROUP));
            }
        });
        searchPanel.add(fighterButton);
        searchPanel.add(groupButton);

    }

    private ListBox buildGroupBox() {
        final ListBox group = new ListBox();
        group.setName("scaGroup");
        if (LookupController.getInstance().getScaGroups() != null) {
            for (ScaGroup g : LookupController.getInstance().getScaGroups()) {
                group.addItem(g.getGroupName(), g.getGroupName());
            }

            group.addChangeHandler(new ChangeHandler() {
                @Override
                public void onChange(ChangeEvent event) {
                    ScaGroup scaGroup = LookupController.getInstance().getScaGroup(group.getValue(group.getSelectedIndex()));
                    logger.info("Changing group to " + scaGroup.getGroupName());
                    fireEvent(new SearchEvent(scaGroup));
                }
            });
        }
        return group;
    }

    @Override
    public void fighterUpdated(Fighter fighter) {
        FighterInfo fi = new FighterInfo();
        fi.setFighterId(fighter.getFighterId());
        fi.setGroup(fighter.getScaGroup().getGroupName());
        fi.setScaName(fighter.getScaName());
        fi.setAuthorizations(getAuthsAsString(fighter.getAuthorization()));
        searchPanel.clear();

        buildBar();
    }

    private String getAuthsAsString(List<Authorization> authorizations) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        if (authorizations != null) {
            for (Authorization a : authorizations) {
                if (first) {
                    first = false;
                } else {
                    sb.append(" ; ");
                }
                sb.append(a.getCode());
            }
        }

        return sb.toString();
    }

    @Override
    public void fighterAdded() {
        searchPanel.clear();

        buildBar();
    }

    @Override
    public void find(String searchName) {
    }

    @Override
    public void loadAll() {
    }

    @Override
    public void loadGroup(ScaGroup group) {
    }

    @Override
    public void switchSearchType(SearchType searchType) {
        if (searchType.equals(SearchType.FIGHTER)) {
            groupBox.getElement().getStyle().setDisplay(Style.Display.NONE);
            searchBox.getElement().getStyle().setDisplay(Style.Display.INLINE);
            submit.getElement().getStyle().setDisplay(Style.Display.INLINE);
            logger.info("switchSearchType: fighter");
            fireEvent(new SearchEvent());
        } else {
            groupBox.getElement().getStyle().setDisplay(Style.Display.INLINE);
            searchBox.getElement().getStyle().setDisplay(Style.Display.NONE);
            submit.getElement().getStyle().setDisplay(Style.Display.NONE);
            ScaGroup scaGroup = LookupController.getInstance().getScaGroup(security.getLoginInfo().getGroup());
            logger.info("switchSearchType: group");

            for (int i = 0; i < groupBox.getItemCount(); ++i) {
                if (groupBox.getItemText(i).equals(scaGroup.getGroupName())) {
                    groupBox.setItemSelected(i, true);
                } else {
                    groupBox.setItemSelected(i, false);
                }
            }
            fireEvent(new SearchEvent(scaGroup));
        }
    }
}
