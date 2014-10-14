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
import org.sca.calontir.cmpe.dto.Report;
import org.sca.calontir.cmpe.dto.ScaGroup;

/**
 *
 * @author rikscarborough
 */
public interface FighterServiceAsync {

    public void countFightersInGroup(String group, AsyncCallback<Integer> async);

    public void countMinorsInGroup(String group, AsyncCallback<Integer> async);

    public void getListItems(Date targetDate, AsyncCallback<FighterListInfo> async);

    public void getFighter(Long id, AsyncCallback<Fighter> async);

    public void getFighters(String cursor, Integer pageSize, Integer offset, AsyncCallback<FighterListResultWrapper> async);

    public void getFightersByGroup(ScaGroup group, String cursor, Integer pageSize, Integer offset, AsyncCallback<FighterListResultWrapper> async);

    public void searchFighters(String searchString, AsyncCallback<FighterListInfo> async);

    public void getFighterByScaName(String scaName, AsyncCallback<Fighter> async);

    public void saveFighter(Fighter fighter, AsyncCallback<Long> async);

    public void getAuthTypes(AsyncCallback<List<AuthType>> async);

    public void getGroups(AsyncCallback<List<ScaGroup>> async);

    public void initialLookup(AsyncCallback<Map<String, Object>> async);

    public void getMinorTotal(String group, AsyncCallback<Integer> async);

    public void sendReportInfo(Map<String, Object> reportInfo, AsyncCallback<Void> async);

    public void getAllReports(AsyncCallback<List<Report>> async);

    public void getReports(Integer days, AsyncCallback<List<Report>> async);

    public void deleteReport(Report report, AsyncCallback<Void> async);

    public void getMinorFighters(String group, AsyncCallback<List<Fighter>> asyncCallback);
}
