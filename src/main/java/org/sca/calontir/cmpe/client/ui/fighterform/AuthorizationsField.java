/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.ui.fighterform;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.sca.calontir.cmpe.client.ui.LookupController;
import org.sca.calontir.cmpe.dto.AuthType;
import org.sca.calontir.cmpe.dto.Authorization;
import org.sca.calontir.cmpe.dto.Fighter;

/**
 *
 * @author rikscarborough
 */
public class AuthorizationsField extends AbstractFieldWidget {

    private final LookupController lookupController = LookupController.getInstance();

    public AuthorizationsField(final Fighter fighter, final boolean edit) {
        Panel dataBody = new FlowPanel();
        dataBody.setStyleName("dataBody");
        if (edit) {
            for (AuthType at : lookupController.getAuthType()) {
                final CheckBox cb = new CheckBox(at.getCode());
                cb.setFormValue(at.getCode());
                cb.setName("authorization");
                if (fighter.getAuthorization() != null) {
                    for (Authorization a : fighter.getAuthorization()) {
                        if (a.getCode().equals(at.getCode())) {
                            cb.setValue(true);
                            break;
                        }
                    }
                }
                cb.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
                    @Override
                    public void onValueChange(ValueChangeEvent<Boolean> event) {
                        List<Authorization> auths = fighter.getAuthorization();
                        if (auths == null) {
                            auths = new ArrayList<Authorization>();
                            fighter.setAuthorization(auths);
                        }
                        Authorization a = new Authorization();
                        a.setCode(cb.getFormValue());
                        if (auths.contains(a)) {
                            if (event.getValue()) {
                                auths.add(a);
                            } else {
                                auths.remove(a);
                            }
                        } else {
                            if (event.getValue()) {
                                auths.add(a);
                            }
                        }
                    }
                });
                dataBody.add(cb);
            }
        } else {
            Label auths = new Label();
            auths.setText(getAuthsAsString(fighter.getAuthorization()));
            dataBody.add(auths);
        }
        initWidget(dataBody);
    }

    private String getAuthsAsString(final List<Authorization> authorizations) {
        final StringBuilder sb = new StringBuilder();
        boolean first = true;
        if (authorizations != null) {
            Collections.sort(authorizations, new Comparator<Authorization>() {
                @Override
                public int compare(Authorization l, Authorization r) {
                    long left = l.getOrderValue() == null ? 99 : l.getOrderValue();
                    long right = r.getOrderValue() == null ? 99 : r.getOrderValue();
                    return (left < right ? -1 : (left == right ? 0 : 1));
                }
            });
            for (Authorization a : authorizations) {
                if (first) {
                    first = false;
                } else {
                    sb.append(" ; ");
                }
                sb.append(a.getCode());
            }
        }

        return sb.toString();
    }
}
