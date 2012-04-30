/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;
import java.util.List;
import org.sca.calontir.cmpe.client.FighterInfo;
import org.sca.calontir.cmpe.client.user.Security;
import org.sca.calontir.cmpe.client.user.SecurityFactory;
import org.sca.calontir.cmpe.common.UserRoles;
import org.sca.calontir.cmpe.dto.Authorization;
import org.sca.calontir.cmpe.dto.Fighter;

/**
 *
 * @author rikscarborough
 */
public class SearchBar extends Composite implements DataUpdatedEventHandler {

	final private Security security = SecurityFactory.getSecurity();
	private Button submit;
	private List<FighterInfo> fighterList;
	FlowPanel searchPanel = new FlowPanel();
	private SuggestBox box;

	public SearchBar() {
		DOM.setElementAttribute(searchPanel.getElement(), "id", "searchBar");

		submit = new Button("Lookup", new ClickHandler() {

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

		searchPanel.add(box);

		searchPanel.add(submit);

		if (security.isRoleOrGreater(UserRoles.CARD_MARSHAL)) {
			Button add = new Button("Add", new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					DOM.getElementById("Signup-Form").getStyle().setDisplay(Style.Display.NONE);
					DOM.getElementById("List-Box").getStyle().setDisplay(Style.Display.NONE);
					DOM.getElementById("FighterForm").getStyle().setDisplay(Style.Display.BLOCK);
					fireEvent(new EditViewEvent(Mode.ADD));
				}
			});

			searchPanel.add(add);
		}

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
