package org.sca.calontir.cmpe.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.sca.calontir.cmpe.dto.AuthType;
import org.sca.calontir.cmpe.dto.Fighter;
import org.sca.calontir.cmpe.dto.Report;
import org.sca.calontir.cmpe.dto.ScaGroup;

@RemoteServiceRelativePath("fighter")
public interface FighterService extends RemoteService {

    public FighterListInfo getListItems(Date targetDate);

    public Fighter getFighter(Long id);

    public FighterListInfo searchFighters(String searchString);

    public Fighter getFighterByScaName(String scaName);

    public FighterListResultWrapper getFighters(String cursor, Integer pageSize, Integer offset);

    public Long saveFighter(Fighter fighter);

    public List<AuthType> getAuthTypes();

    public List<ScaGroup> getGroups();

    public Map<String, Object> initialLookup();

    public Integer getMinorTotal(String group);

    public List<Fighter> getMinorFighters(String group);

    public void sendReportInfo(Map<String, Object> reportInfo);

    public List<Report> getAllReports();

    public List<Report> getReports(Integer days);

    public void deleteReport(Report report);
}
