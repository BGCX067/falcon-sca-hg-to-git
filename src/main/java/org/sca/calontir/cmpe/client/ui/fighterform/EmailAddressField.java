package org.sca.calontir.cmpe.client.ui.fighterform;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import java.util.ArrayList;
import java.util.List;
import org.sca.calontir.cmpe.dto.Email;
import org.sca.calontir.cmpe.dto.Fighter;

/**
 *
 * @author rikscarborough
 */
public class EmailAddressField extends AbstractFieldWidget {

	public EmailAddressField(final Fighter fighter, final boolean edit) {
		if (edit) {
			final TextBox email = new TextBox();
			email.setName("email");
			email.setVisibleLength(25);
			email.setStyleName("email");
			if (fighter.getEmail() != null && !fighter.getEmail().isEmpty()) {
				email.setValue(fighter.getEmail().get(0).getEmailAddress());
			}
			email.addValueChangeHandler(new ValueChangeHandler<String>() {
				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					String changed = cleanString(event.getValue());
					if (fighter.getEmail() == null || fighter.getEmail().isEmpty()) {
						Email e = new Email();
						e.setEmailAddress(changed);
						List<Email> emails = new ArrayList<Email>();
						emails.add(e);
						fighter.setEmail(emails);
					} else {
						fighter.getEmail().get(0).setEmailAddress(changed);
					}
				}
			});
			initWidget(email);
		} else {
			if (fighter.getEmail() != null && !fighter.getEmail().isEmpty()) {
				String emailAddress = fighter.getEmail().get(0).getEmailAddress();
				initWidget(new Anchor(emailAddress, "mailto:" + emailAddress, "_blank"));
			} else {
				initWidget(new Label(""));
			}
		}
	}
	
}
