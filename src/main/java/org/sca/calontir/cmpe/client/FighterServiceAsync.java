/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.sca.calontir.cmpe.dto.AuthType;
import org.sca.calontir.cmpe.dto.Fighter;
import org.sca.calontir.cmpe.dto.ScaGroup;

/**
 *
 * @author rikscarborough
 */
public interface FighterServiceAsync {
    public void getListItems(Date targetDate, AsyncCallback<FighterListInfo> async);

    public void getFighter(Long id, AsyncCallback<Fighter> async);

	public void getFighterByScaName(String scaName, AsyncCallback<Fighter> async);
    
    public void getAuthTypes(AsyncCallback<List<AuthType>> async);
    
    public void getGroups(AsyncCallback<List<ScaGroup>> async);

	public void initialLookup(AsyncCallback<Map<String, Object>> async);

	public void getMinorTotal(String group, AsyncCallback<Integer> async);

	public void sendReportInfo(Map<String, Object> reportInfo, AsyncCallback<Void> async);
}
