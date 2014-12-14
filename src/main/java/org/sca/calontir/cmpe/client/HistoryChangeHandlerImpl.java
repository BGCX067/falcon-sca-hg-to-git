package org.sca.calontir.cmpe.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

/**
 *
 * @author rikscarborough
 */
class HistoryChangeHandlerImpl implements ValueChangeHandler<String> {

    public HistoryChangeHandlerImpl() {
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        String historyToken = event.getValue();
        try {
            if (historyToken.substring(0, 8).equals("display:")) {
                String display = historyToken.substring(8);
                // Select the specified tab panel
                DisplayUtils.changeDisplay(DisplayUtils.Displays.valueOf(display));
            } else if (historyToken.startsWith("qrtlyreport:")) {
                DisplayUtils.clearDisplay();
                DisplayUtils.changeDisplay(DisplayUtils.Displays.ReportGen);
            } else {
                DisplayUtils.resetDisplay();
                DisplayUtils.changeDisplay(DisplayUtils.Displays.SignupForm);
            }
        } catch (IndexOutOfBoundsException e) {
            DisplayUtils.changeDisplay(DisplayUtils.Displays.SignupForm);
        }
    }
}
