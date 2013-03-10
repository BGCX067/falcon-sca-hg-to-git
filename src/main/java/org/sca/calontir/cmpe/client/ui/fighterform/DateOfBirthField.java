/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.ui.fighterform;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.datepicker.client.DateBox;
import java.util.Date;
import org.sca.calontir.cmpe.dto.Fighter;

/**
 *
 * @author rikscarborough
 */
public class DateOfBirthField extends AbstractFieldWidget {

	public DateOfBirthField(final Fighter fighter, final boolean edit) {
		if (edit) {
			final DateBox dateOfBirth = new DateBox();
			dateOfBirth.getTextBox().getElement().setId("dateOfBirth");
			dateOfBirth.getTextBox().setName("dateOfBirth");
			dateOfBirth.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("MM/dd/yyyy")));
			dateOfBirth.setStyleName("dateOfBirth");
			if (fighter.getDateOfBirth() != null) {
				dateOfBirth.setValue(DateTimeFormat.getFormat("MM/dd/yyyy").parse(fighter.getDateOfBirth()));
			}
			dateOfBirth.addValueChangeHandler(new ValueChangeHandler<Date>() {
				@Override
				public void onValueChange(ValueChangeEvent<Date> event) {
					if (dateOfBirth.getTextBox().getValue().isEmpty()) {
						fighter.setDateOfBirth(null);
					} else {
						fighter.setDateOfBirth(dateOfBirth.getTextBox().getValue());
					}
//					dateOfBirth.getElement().getStyle().setColor("black");
				}
			});
//			dateOfBirth.getTextBox().addFocusHandler(new FocusHandler() {
//
//				@Override
//				public void onFocus(FocusEvent event) {
//					if (dateOfBirth.getTextBox().getValue().isEmpty()) {
//						ghost.value = true;
//						Date sixteen = new Date();
//						sixteen.setYear(sixteen.getYear() - 16);
//						dateOfBirth.setValue(sixteen, false);
//						dateOfBirth.getElement().getStyle().setColor("lightgrey");
//						//dateOfBirth.getDatePicker().setCurrentMonth(sixteen);
//					}
//				}
//			});
			dateOfBirth.getTextBox().addBlurHandler(new BlurHandler() {
				@Override
				public void onBlur(BlurEvent event) {
					if (dateOfBirth.getTextBox().getValue().isEmpty() && fighter.getDateOfBirth() != null) {
						fighter.setDateOfBirth(null);
						dateOfBirth.setValue(null, false);
					}
//					if (ghost.value) {
//						dateOfBirth.setValue(null, false);
//						dateOfBirth.getTextBox().setValue("", false);
//					}
//					dateOfBirth.getElement().getStyle().setColor("black");
					//dateOfBirth.setValue(fighter.getDateOfBirth());
				}
			});
			initWidget(dateOfBirth);
		} else if (fighter.getDateOfBirth() != null) {
			initWidget(new Label(fighter.getDateOfBirth()));
		} else {
			initWidget(new Label(""));
		}
	}
}
