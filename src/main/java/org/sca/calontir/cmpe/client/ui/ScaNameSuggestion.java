/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.SuggestOracle;

/**
 *
 * @author rikscarborough
 */
public class ScaNameSuggestion implements IsSerializable, SuggestOracle.Suggestion {

    private String scaName;

    public ScaNameSuggestion() {
    }

    public ScaNameSuggestion(String scaName) {
        this.scaName = scaName;
    }

    @Override
    public String getDisplayString() {
        return scaName;
    }

    @Override
    public String getReplacementString() {
        return scaName;
    }

}
