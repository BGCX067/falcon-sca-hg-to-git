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
public class MembershipExpiresField extends AbstractFieldWidget {

	public MembershipExpiresField(final Fighter fighter, final boolean edit) {
		if (edit) {
			final DateBox membershipExpires = new DateBox();
			membershipExpires.getTextBox().getElement().setId("membershipExpires");
			membershipExpires.getTextBox().setName("membershipExpires");
			membershipExpires.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("MM/dd/yyyy")));
			membershipExpires.setStyleName("membershipExpires");
			if (fighter.getMembershipExpires() != null) {
				membershipExpires.setValue(DateTimeFormat.getFormat("MM/dd/yyyy").parse(fighter.getMembershipExpires()));
			}
			membershipExpires.addValueChangeHandler(new ValueChangeHandler<Date>() {
				@Override
				public void onValueChange(ValueChangeEvent<Date> event) {
					if (membershipExpires.getTextBox().getValue().isEmpty()) {
						fighter.setMembershipExpires(null);
					} else {
						fighter.setMembershipExpires(membershipExpires.getTextBox().getValue());
					}
				}
			});
			membershipExpires.getTextBox().addBlurHandler(new BlurHandler() {
				@Override
				public void onBlur(BlurEvent event) {
					if (membershipExpires.getTextBox().getValue().isEmpty() && fighter.getMembershipExpires() != null) {
						fighter.setMembershipExpires(null);
						membershipExpires.setValue(null, false);
					}
				}
			});
			initWidget(membershipExpires);
		} else if (fighter.getMembershipExpires() != null) {
			initWidget(new Label(fighter.getMembershipExpires()));
		} else {
			initWidget(new Label(""));
		}
	}
	
}
