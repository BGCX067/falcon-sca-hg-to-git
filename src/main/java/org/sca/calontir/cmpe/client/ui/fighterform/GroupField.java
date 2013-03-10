/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.ui.fighterform;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import org.sca.calontir.cmpe.client.ui.FighterFormWidget;
import org.sca.calontir.cmpe.client.ui.LookupController;
import org.sca.calontir.cmpe.dto.Fighter;
import org.sca.calontir.cmpe.dto.ScaGroup;

/**
 *
 * @author rikscarborough
 */
public class GroupField extends AbstractFieldWidget {

	private LookupController lookupController = LookupController.getInstance();

	public GroupField(final Fighter fighter, final boolean edit, final boolean add) {
		if (edit) {
			final ListBox group = new ListBox();
			group.setName("scaGroup");
			if (add) {
				group.addItem("Select a group", "SELECTGROUP");
			}
			for (ScaGroup g : lookupController.getScaGroups()) {
				group.addItem(g.getGroupName(), g.getGroupName());
			}

			if (fighter.getScaGroup() != null) {
				for (int i = 0; i < group.getItemCount(); ++i) {
					if (group.getValue(i).equals(fighter.getScaGroup().getGroupName())) {
						group.setSelectedIndex(i);
						break;
					}
				}
			}
			group.addChangeHandler(new ChangeHandler() {
				@Override
				public void onChange(ChangeEvent event) {
					for (ScaGroup g : lookupController.getScaGroups()) {
						if (g.getGroupName().equals(group.getValue(group.getSelectedIndex()))) {
							fighter.setScaGroup(g);
							break;
						}
					}
				}
			});
			initWidget(group);
		} else {
			initWidget(new Label(fighter.getScaGroup() == null ? "" : fighter.getScaGroup().getGroupName()));
		}
	}
	
}
