package org.sca.calontir.cmpe.client.ui.fighterform;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import org.sca.calontir.cmpe.common.UserRoles;
import org.sca.calontir.cmpe.dto.Fighter;

/**
 *
 * @author rikscarborough
 */
public class ModernName extends AbstractFieldWidget {

    public ModernName(final Fighter fighter, final boolean edit) {
        if (edit && security.isRoleOrGreater(UserRoles.CARD_MARSHAL)) {
            final TextBox modernName = new TextBox();
            modernName.setName("modernName");
            modernName.getElement().setAttribute("id", "modernName");
            modernName.setVisibleLength(25);
            modernName.setStyleName("modernName");
            modernName.setValue(fighter.getModernName());
            modernName.addValueChangeHandler(new ValueChangeHandler<String>() {
                @Override
                public void onValueChange(ValueChangeEvent<String> event) {
                    String change = cleanString(event.getValue());
                    fighter.setModernName(modernName.getValue().trim());
                    //TODO: error, this cannot be blank
                }
            });
            initWidget(modernName);

        } else {
            initWidget(new Label(fighter.getModernName()));
        }
    }
}
