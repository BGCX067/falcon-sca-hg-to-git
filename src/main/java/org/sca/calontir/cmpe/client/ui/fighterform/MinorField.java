/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.ui.fighterform;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Label;
import java.util.Date;
import org.sca.calontir.cmpe.dto.Fighter;

/**
 *
 * @author rikscarborough
 */
public class MinorField extends AbstractFieldWidget {

    public MinorField(final Fighter fighter, final boolean edit) {
        if (edit) {
            initWidget(new Label(""));
        } else {
            if (fighter.getDateOfBirth() != null) {
                Date d = DateTimeFormat.getFormat("MM/dd/yyyy").parse(fighter.getDateOfBirth());
                initWidget(new Label(isMinor(d) ? "true" : "false"));
            } else {
                initWidget(new Label("false"));
            }
        }

    }

    @SuppressWarnings(/* Required to use Date API in gwt */{"deprecation"})
    private boolean isMinor(Date dob) {
        final Date now = new Date();
        final Date targetDate = new Date(dob.getYear() + 18, dob.getMonth(), dob.getDate());
        return targetDate.after(now);
    }
}
