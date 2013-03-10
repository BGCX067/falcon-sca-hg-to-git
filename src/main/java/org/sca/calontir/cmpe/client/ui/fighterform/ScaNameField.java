package org.sca.calontir.cmpe.client.ui.fighterform;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TextBox;
import org.sca.calontir.cmpe.dto.Fighter;

/**
 *
 * @author rikscarborough
 */
public class ScaNameField extends AbstractFieldWidget {

	public ScaNameField(final Fighter fighter, final boolean edit) {
		if (edit) {
			final TextBox scaNameTextBox = new TextBox();
			scaNameTextBox.setName("scaName");
			scaNameTextBox.getElement().setId("scaName");
			scaNameTextBox.setVisibleLength(25);
			scaNameTextBox.setStyleName("scaName");
			scaNameTextBox.setValue(fighter.getScaName());
			scaNameTextBox.addValueChangeHandler(new ValueChangeHandler<String>() {
				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					//validate
					String change = cleanString(event.getValue());
					fighter.setScaName(change);
				}
			});
			initWidget(scaNameTextBox);

		} else {
			InlineLabel scaName = new InlineLabel();
			scaName.setText(fighter.getScaName());
			scaName.setStyleName("scaName");
			initWidget(scaName);
		}
	}
}
