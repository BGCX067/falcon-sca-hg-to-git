/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.ui.fighterform;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import org.sca.calontir.cmpe.dto.Fighter;

/**
 *
 * @author rikscarborough
 */
public class ScaMemberNoField extends AbstractFieldWidget {

	public ScaMemberNoField(final Fighter fighter, final boolean edit) {
		if (edit) {
			final TextBox scaMemberNo = new TextBox();
			scaMemberNo.setName("scaMemberNo");
			scaMemberNo.setVisibleLength(20);
			scaMemberNo.setStyleName("scaMemberNo");
			scaMemberNo.setValue(fighter.getScaMemberNo());
			scaMemberNo.addValueChangeHandler(new ValueChangeHandler<String>() {
				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					String changed = cleanString(event.getValue());
					fighter.setScaMemberNo(changed);
				}
			});
			initWidget(scaMemberNo);
		} else {
			initWidget(new Label(fighter.getScaMemberNo()));
		}
	}
	
}
