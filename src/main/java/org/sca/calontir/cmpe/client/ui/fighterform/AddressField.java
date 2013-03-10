/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.ui.fighterform;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import java.util.ArrayList;
import java.util.List;
import org.sca.calontir.cmpe.dto.Address;
import org.sca.calontir.cmpe.dto.Fighter;

/**
 *
 * @author rikscarborough
 */
public class AddressField extends AbstractFieldWidget {

	public AddressField(final Fighter fighter, final boolean edit) {
		final Address address;
		if (fighter.getAddress() != null && !fighter.getAddress().isEmpty()) {
			address = fighter.getAddress().get(0);
		} else {
			address = new Address();
			List<Address> addresses = new ArrayList<Address>();
			addresses.add(address);
			fighter.setAddress(addresses);
		}
		if (edit) {
			final FlexTable addressTable = new FlexTable();
			addressTable.setText(0, 0, "Street:");
			final TextBox address1 = new TextBox();
			address1.setName("address1");
			address1.setValue(address.getAddress1());
			address1.setVisibleLength(60);
			address1.addValueChangeHandler(new ValueChangeHandler<String>() {
				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					String changed = cleanString(event.getValue());
					address.setAddress1(changed);
					fighter.getAddress().get(0).setAddress1(changed);
					address1.setValue(changed);
				}
			});
			addressTable.setWidget(0, 1, address1);

			addressTable.setText(1, 0, "Line 2:");
			final TextBox address2 = new TextBox();
			address2.setName("address2");
			address2.setValue(address.getAddress2());
			address2.setVisibleLength(60);
			address2.addValueChangeHandler(new ValueChangeHandler<String>() {
				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					String changed = cleanString(event.getValue());
					address.setAddress2(changed);
					fighter.getAddress().get(0).setAddress2(changed);
					address2.setValue(changed);
				}
			});
			addressTable.setWidget(1, 1, address2);

			addressTable.setText(2, 0, "City:");
			final TextBox city = new TextBox();
			city.setName("city");
			city.setValue(address.getCity());
			city.setVisibleLength(30);
			city.addValueChangeHandler(new ValueChangeHandler<String>() {
				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					String changed = cleanString(event.getValue());
					address.setCity(changed);
					fighter.getAddress().get(0).setCity(changed);
					city.setValue(changed);
				}
			});
			addressTable.setWidget(2, 1, city);

			addressTable.setText(3, 0, "State:");
			final TextBox state = new TextBox();
			state.setName("state");
			state.setValue(address.getState());
			state.setVisibleLength(20);
			state.addValueChangeHandler(new ValueChangeHandler<String>() {
				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					String changed = cleanString(event.getValue());
					address.setState(changed);
					fighter.getAddress().get(0).setState(changed);
					state.setValue(changed);
				}
			});
			addressTable.setWidget(3, 1, state);

			addressTable.setText(4, 0, "Postal Code:");
			final TextBox postalCode = new TextBox();
			postalCode.setName("postalCode");
			postalCode.setValue(address.getPostalCode());
			postalCode.setVisibleLength(30);
			postalCode.addValueChangeHandler(new ValueChangeHandler<String>() {
				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					String changed = cleanString(event.getValue());
					address.setPostalCode(changed);
					fighter.getAddress().get(0).setPostalCode(changed);
					postalCode.setValue(changed);
				}
			});
			addressTable.setWidget(4, 1, postalCode);

			initWidget(addressTable);
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append(address.getAddress1());
			if (address.getAddress2() != null && !address.getAddress2().isEmpty()) {
				sb.append(", ");
				sb.append(address.getAddress2());
			}
			sb.append(", ");
			sb.append(address.getCity());
			sb.append(", ");
			sb.append(address.getState());
			sb.append("  ");
			sb.append(address.getPostalCode());
			initWidget(new Label(sb.toString()));
		}
	}
	
}
