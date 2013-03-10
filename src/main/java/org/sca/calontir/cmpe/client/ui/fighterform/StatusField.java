/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.ui.fighterform;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import org.sca.calontir.cmpe.common.FighterStatus;
import org.sca.calontir.cmpe.common.UserRoles;
import org.sca.calontir.cmpe.dto.Fighter;

/**
 *
 * @author rikscarborough
 */
public class StatusField extends AbstractFieldWidget {

	public StatusField(final Fighter fighter, final boolean edit) {
		if (edit && security.isRoleOrGreater(UserRoles.CARD_MARSHAL)) {
			final ListBox status = new ListBox();
			status.setName("fighterStatus");
			for (FighterStatus f_status : FighterStatus.values()) {
				status.addItem(f_status.toString(), f_status.toString());
			}

			for (int i = 0; i < status.getItemCount(); ++i) {
				if (status.getValue(i).equals(fighter.getStatus().toString())) {
					status.setSelectedIndex(i);
					break;
				}
			}
			status.addChangeHandler(new ChangeHandler() {
				@Override
				public void onChange(ChangeEvent event) {
					fighter.setStatus(FighterStatus.valueOf(status.getValue(status.getSelectedIndex())));
				}
			});
			initWidget(status);
		} else {
			initWidget(new Label(fighter.getStatus().toString()));
		}
	}
	
}
