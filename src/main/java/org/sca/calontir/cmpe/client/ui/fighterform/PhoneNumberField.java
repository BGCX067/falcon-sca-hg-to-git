package org.sca.calontir.cmpe.client.ui.fighterform;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import java.util.ArrayList;
import java.util.List;
import org.sca.calontir.cmpe.dto.Fighter;
import org.sca.calontir.cmpe.dto.Phone;

/**
 *
 * @author rikscarborough
 */
public class PhoneNumberField extends AbstractFieldWidget {

	public PhoneNumberField(final Fighter fighter, final boolean edit) {
		if (edit) {
			final TextBox phoneNumber = new TextBox();
			phoneNumber.setName("phoneNumber");
			phoneNumber.setVisibleLength(25);
			phoneNumber.setStyleName("phoneNumber");
			if (fighter.getPhone() != null && !fighter.getPhone().isEmpty()) {
				phoneNumber.setValue(fighter.getPhone().get(0).getPhoneNumber());
			}
			phoneNumber.addValueChangeHandler(new ValueChangeHandler<String>() {
				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					if (fighter.getPhone() == null || fighter.getPhone().isEmpty()) {
						Phone phone = new Phone();
						if (phoneNumber.getValue() == null) {
							phone.setPhoneNumber("");
						} else {
							phone.setPhoneNumber(phoneNumber.getValue().trim());
						}
						List<Phone> phones = new ArrayList<Phone>();
						phones.add(phone);
						fighter.setPhone(phones);
					} else {
						fighter.getPhone().get(0).setPhoneNumber(phoneNumber.getValue());
					}
				}
			});
			initWidget(phoneNumber);
		} else {
			if (fighter.getPhone() != null && !fighter.getPhone().isEmpty()) {
				initWidget(new Label(fighter.getPhone().get(0).getPhoneNumber()));
			} else {
				initWidget(new Label(""));
			}
		}
	}
	
}
