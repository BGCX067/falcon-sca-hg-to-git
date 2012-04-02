/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.Date;
import org.sca.calontir.cmpe.dto.Fighter;

/**
 *
 * @author rikscarborough
 */
public interface FighterServiceAsync {
    public void getListItems(Date targetDate, AsyncCallback<FighterListInfo> async);

    public void getFighter(Long id, AsyncCallback<Fighter> async);
}
