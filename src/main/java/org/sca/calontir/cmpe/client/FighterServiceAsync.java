/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;

/**
 *
 * @author rikscarborough
 */
public interface FighterServiceAsync {
    public void getListItems(AsyncCallback<List<FighterListInfo>> async);
}
