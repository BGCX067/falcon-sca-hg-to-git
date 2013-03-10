package org.sca.calontir.cmpe.client.ui.fighterform;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import org.sca.calontir.cmpe.common.UserRoles;
import org.sca.calontir.cmpe.dto.Fighter;
import org.sca.calontir.cmpe.dto.Treaty;

/**
 *
 * @author rikscarborough
 */
public class TreatyField extends AbstractFieldWidget {

	public TreatyField(final Fighter fighter, final boolean edit) {
		if (edit && security.isRoleOrGreater(UserRoles.CARD_MARSHAL)) {
			final CheckBox treaty = new CheckBox("Treaty");
			treaty.setName("treaty");
			if (fighter.getTreaty() != null) {
				treaty.setValue(true);
			}
			treaty.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					if (event.getValue()) {
						Treaty t = new Treaty();
						t.setName("Treaty");
						fighter.setTreaty(t);
					} else {
						fighter.setTreaty(null);
					}
				}
			});
			initWidget(treaty);
		} else if (fighter.getTreaty() != null) {
			initWidget(new Label("Treaty"));
		} else {
			initWidget(new Label(""));
		}
	}
}
