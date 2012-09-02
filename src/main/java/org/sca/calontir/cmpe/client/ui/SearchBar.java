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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import java.util.List;
import org.sca.calontir.cmpe.client.FighterInfo;
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
public class SearchBar extends Composite implements DataUpdatedEventHandler {

	private static final String SEARCH = "Search";
	final private Security security = SecurityFactory.getSecurity();
	private Button submit;
	private List<FighterInfo> fighterList;
	FlowPanel searchPanel = new FlowPanel();
	private SuggestBox box;
	private ListBox groupBox;

	public SearchBar() {
		DOM.setElementAttribute(searchPanel.getElement(), "id", "searchBar");

		submit = new Button(SEARCH, new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				submit.setEnabled(false);


				String searchName = box.getText();
				if (searchName == null || searchName.trim().length() == 0) {
					fireEvent(new SearchEvent());
				} else {
					fireEvent(new SearchEvent(searchName));
				}
				submit.setEnabled(true);
			}
		});

		buildBar();

		initWidget(searchPanel);
	}

	private void buildBar() {
		box = buildSuggestBox();
		DOM.setElementAttribute(box.getElement(), "id", "search");
		DOM.setElementAttribute(box.getElement(), "autocomplete", "off");

		groupBox = buildGroupBox();
		DOM.setElementAttribute(box.getElement(), "id", "groupBox");
		groupBox.getElement().getStyle().setDisplay(Style.Display.NONE);

		searchPanel.add(box);
		searchPanel.add(groupBox);

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
				if (event.getValue()) {
					groupBox.getElement().getStyle().setDisplay(Style.Display.NONE);
					box.getElement().getStyle().setDisplay(Style.Display.INLINE);
					submit.getElement().getStyle().setDisplay(Style.Display.INLINE);
					fireEvent(new SearchEvent(SearchEvent.SearchType.FIGHTER));
					fireEvent(new SearchEvent());
				}
			}
		});
		RadioButton groupButton = new RadioButton("searchGroup", "group");
		groupButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					List<FighterInfo> fList = LookupController.getInstance().getFighterList(security.getLoginInfo().getScaName());
					FighterInfo user = fList.get(0);
					if (user.getGroup() != null) {
						for (int i = 0; i < groupBox.getItemCount(); ++i) {
							if (groupBox.getValue(i).equals(user.getGroup())) {
								groupBox.setSelectedIndex(i);
								break;
							}
						}
					}
					groupBox.getElement().getStyle().setDisplay(Style.Display.INLINE);
					box.getElement().getStyle().setDisplay(Style.Display.NONE);
					submit.getElement().getStyle().setDisplay(Style.Display.NONE);
					fireEvent(new SearchEvent(SearchEvent.SearchType.GROUP));
					ScaGroup scaGroup = LookupController.getInstance().getScaGroup(user.getGroup());
					fireEvent(new SearchEvent(scaGroup));
				}
			}
		});
		searchPanel.add(fighterButton);
		searchPanel.add(groupButton);

	}

	private ListBox buildGroupBox() {
		final ListBox group = new ListBox();
		group.setName("scaGroup");
		for (ScaGroup g : LookupController.getInstance().getScaGroups()) {
			group.addItem(g.getGroupName(), g.getGroupName());
		}

		List<FighterInfo> fList = LookupController.getInstance().getFighterList(security.getLoginInfo().getScaName());
		FighterInfo user = fList.get(0);
		if (user.getGroup() != null) {
			for (int i = 0; i < group.getItemCount(); ++i) {
				if (group.getValue(i).equals(user.getGroup())) {
					group.setSelectedIndex(i);
					break;
				}
			}
		}
		group.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				ScaGroup scaGroup = LookupController.getInstance().getScaGroup(group.getValue(group.getSelectedIndex()));
				fireEvent(new SearchEvent(scaGroup));
			}
		});
		return group;
	}

	private SuggestBox buildSuggestBox() {
		final MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
		fighterList = LookupController.getInstance().getFighterList(null);
		for (FighterInfo fi : fighterList) {
			oracle.add(fi.getScaName());
		}
		return new SuggestBox(oracle);
	}

	@Override
	public void fighterUpdated(Fighter fighter) {
		FighterInfo fi = new FighterInfo();
		fi.setFighterId(fighter.getFighterId());
		fi.setGroup(fighter.getScaGroup().getGroupName());
		fi.setScaName(fighter.getScaName());
		fi.setAuthorizations(getAuthsAsString(fighter.getAuthorization()));
		LookupController.getInstance().replaceFighter(fi);
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
		LookupController.getInstance().replaceFighter(null);
		searchPanel.clear();

		buildBar();
	}
}
