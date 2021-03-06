package org.sca.calontir.cmpe.server;

import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.backends.BackendServiceFactory;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;
import org.sca.calontir.cmpe.ValidationException;
import org.sca.calontir.cmpe.client.FighterInfo;
import org.sca.calontir.cmpe.client.FighterListInfo;
import org.sca.calontir.cmpe.client.FighterListResultWrapper;
import org.sca.calontir.cmpe.client.FighterService;
import org.sca.calontir.cmpe.common.UserRoles;
import org.sca.calontir.cmpe.db.AuthTypeDAO;
import org.sca.calontir.cmpe.db.FighterDAO;
import org.sca.calontir.cmpe.db.ReportDAO;
import org.sca.calontir.cmpe.db.ScaGroupDAO;
import org.sca.calontir.cmpe.db.TableUpdatesDao;
import org.sca.calontir.cmpe.dto.AuthType;
import org.sca.calontir.cmpe.dto.Fighter;
import org.sca.calontir.cmpe.dto.FighterListItem;
import org.sca.calontir.cmpe.dto.FighterResultWrapper;
import org.sca.calontir.cmpe.dto.Report;
import org.sca.calontir.cmpe.dto.ScaGroup;
import org.sca.calontir.cmpe.dto.TableUpdates;
import org.sca.calontir.cmpe.user.Security;
import org.sca.calontir.cmpe.user.SecurityFactory;

/**
 *
 * @author rikscarborough
 */
public class FighterServiceImpl extends RemoteServiceServlet implements FighterService {

    private static final Logger log = Logger.getLogger(FighterServiceImpl.class.getName());

    @Override
    public FighterListInfo getListItems(Date targetDate) {
        FighterListInfo retval = new FighterListInfo();
        FighterDAO fighterDao = new FighterDAO();
        TableUpdatesDao tuDao = new TableUpdatesDao();
        TableUpdates tu = tuDao.getTableUpdates("Fighter");
        List<FighterListItem> fighters;
        fighters = fighterDao.getFighterListItems(new DateTime(targetDate));
        retval.setUpdateInfo(true);

        return convert(fighters);
    }

    private FighterListInfo convert(List<FighterListItem> fighters) {
        final FighterListInfo retval = new FighterListInfo();
        final List<FighterInfo> retValList = new ArrayList<>();
        for (FighterListItem fli : fighters) {
            if (fli != null) {
                final FighterInfo info = new FighterInfo();
                info.setFighterId(fli.getFighterId() == null ? 0 : fli.getFighterId());
                info.setScaName(fli.getScaName() == null ? "" : fli.getScaName());
                info.setAuthorizations(fli.getAuthorizations() == null ? "" : fli.getAuthorizations());
                info.setGroup(fli.getGroup() == null ? "" : fli.getGroup());
                info.setStatus(fli.getStatus() == null ? "" : fli.getStatus().toString());
                info.setMinor(fli.isMinor());
                info.setRole(fli.getRole() == null ? "" : fli.getRole());
                retValList.add(info);
            }
        }
        retval.setFighterInfo(retValList);

        return retval;
    }

    @Override
    public Fighter getFighter(Long id) {
        FighterDAO fighterDao = new FighterDAO();

        return fighterDao.getFighter(id);
    }

    @Override
    public FighterListResultWrapper getFighters(String cursor, Integer pageSize, Integer offset) {
        final Security security = SecurityFactory.getSecurity();
        final FighterDAO fighterDao = new FighterDAO();
        FighterResultWrapper fighterResults;
        if (cursor == null) {
            fighterResults = fighterDao.getFighters(pageSize, offset, security.isRoleOrGreater(UserRoles.CARD_MARSHAL));
        } else {
            Cursor startCursor = Cursor.fromWebSafeString(cursor);
            fighterResults = fighterDao.getFighters(pageSize, startCursor, security.isRoleOrGreater(UserRoles.CARD_MARSHAL));
        }
        final String newCursor = fighterResults.getCursor() == null ? null : fighterResults.getCursor().toWebSafeString();

        final FighterListResultWrapper fighterListResults = new FighterListResultWrapper();
        fighterListResults.setFighters(convert(fighterResults.getFighters()));
        fighterListResults.setCursor(newCursor);
        fighterListResults.setPageSize(pageSize);
        fighterListResults.setCount(fighterDao.getTotalCount());
        return fighterListResults;
    }

    @Override
    public FighterListResultWrapper getFightersByGroup(ScaGroup group, String cursor, Integer pageSize, Integer offset) {
        final Security security = SecurityFactory.getSecurity();
        final FighterDAO fighterDao = new FighterDAO();
        final FighterResultWrapper fighterResults;
        if (cursor == null) {
            fighterResults = fighterDao.getFightersByGroup(group, pageSize, offset, security.isRoleOrGreater(UserRoles.CARD_MARSHAL));
        } else {
            final Cursor startCursor = Cursor.fromWebSafeString(cursor);
            fighterResults = fighterDao.getFightersByGroup(group, pageSize, startCursor, security.isRoleOrGreater(UserRoles.CARD_MARSHAL));
        }
        final String newCursor = fighterResults.getCursor() == null ? null : fighterResults.getCursor().toWebSafeString();

        final FighterListResultWrapper fighterListResults = new FighterListResultWrapper();
        fighterListResults.setFighters(convert(fighterResults.getFighters()));
        fighterListResults.setCursor(newCursor);
        fighterListResults.setPageSize(pageSize);
        fighterListResults.setCount(fighterDao.getFighterCountInGroup(group));
        return fighterListResults;
    }

    @Override
    public FighterListResultWrapper getFightersSortedByScaName(Integer pageSize) {
        final FighterDAO fighterDao = new FighterDAO();
        FighterResultWrapper fighterResults = fighterDao.getFightersSortedByScaName(pageSize);
        final String newCursor = fighterResults.getCursor().toWebSafeString();

        final FighterListResultWrapper fighterListResults = new FighterListResultWrapper();
        fighterListResults.setFighters(convert(fighterResults.getFighters()));
        fighterListResults.setCursor(newCursor);
        fighterListResults.setPageSize(pageSize);
        fighterListResults.setCount(fighterDao.getTotalCount());
        return fighterListResults;
    }

    @Override
    public FighterListResultWrapper getFightersSortedByScaGroup(Integer pageSize) {
        final FighterDAO fighterDao = new FighterDAO();
        FighterResultWrapper fighterResults = fighterDao.getFightersSortedByGroup(pageSize);
        final String newCursor = fighterResults.getCursor().toWebSafeString();

        final FighterListResultWrapper fighterListResults = new FighterListResultWrapper();
        fighterListResults.setFighters(convert(fighterResults.getFighters()));
        fighterListResults.setCursor(newCursor);
        fighterListResults.setPageSize(pageSize);
        fighterListResults.setCount(fighterDao.getTotalCount());
        return fighterListResults;
    }

    @Override
    public FighterListResultWrapper getFightersSortedByStatus(Integer pageSize) {
        final FighterDAO fighterDao = new FighterDAO();
        FighterResultWrapper fighterResults = fighterDao.getFightersSortedByStatus(pageSize);
        final String newCursor = fighterResults.getCursor().toWebSafeString();

        final FighterListResultWrapper fighterListResults = new FighterListResultWrapper();
        fighterListResults.setFighters(convert(fighterResults.getFighters()));
        fighterListResults.setCursor(newCursor);
        fighterListResults.setPageSize(pageSize);
        fighterListResults.setCount(fighterDao.getTotalCount());
        return fighterListResults;
    }

    @Override
    public Long saveFighter(Fighter fighter) {
        FighterDAO fighterDao = new FighterDAO();
        try {
            return fighterDao.saveFighter(fighter, fighter.getFighterId(), false);
        } catch (ValidationException ex) {
            log.log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public FighterListInfo searchFighters(String searchString) {
        final Security security = SecurityFactory.getSecurity();
        if (!security.isRoleOrGreater(UserRoles.CARD_MARSHAL)) {
            searchString = "scaName = " + searchString;
        }
        final FighterDAO fighterDao = new FighterDAO();
        return convert(fighterDao.searchFighters(searchString));
    }

    @Override
    public List<AuthType> getAuthTypes() {
        AuthTypeDAO dao = new AuthTypeDAO();
        return dao.getAuthType();
    }

    @Override
    public List<ScaGroup> getGroups() {
        ScaGroupDAO dao = new ScaGroupDAO();
        return dao.getScaGroup();
    }

    @Override
    public Fighter getFighterByScaName(String scaName) {
        FighterDAO fighterDao = new FighterDAO();
        List<Fighter> fList = fighterDao.queryFightersByScaName(scaName);
        if (fList != null && !fList.isEmpty()) {
            return fList.get(0);
        }
        return null;
    }

    @Override
    public Map<String, Object> initialLookup() {
        log.log(Level.INFO, "Start Initial Lookup");
        Map<String, Object> iMap = new HashMap<>();
        // get application version
        iMap.put("appversion", "1.3.2");

        // get groups
        ScaGroupDAO groupDao = new ScaGroupDAO();
        List<ScaGroup> groups = groupDao.getScaGroup();
        iMap.put("groups", groups);

        // get authtypes
        AuthTypeDAO authTypeDao = new AuthTypeDAO();
        List<AuthType> authTypes = authTypeDao.getAuthType();
        iMap.put("authTypes", authTypes);

        return iMap;
    }

    @Override
    public Integer getMinorTotal(String group) {
        int ret = 0;
        FighterDAO fighterDao = new FighterDAO();
        ScaGroupDAO groupDao = new ScaGroupDAO();
        //ScaGroup scaGroup = groupDao.getScaGroupByName(group);
        List<Fighter> fList = fighterDao.getMinorCount();
        for (Fighter f : fList) {
            if (f.getScaGroup().getGroupName().equals(group)) {
                ++ret;
            }
        }
        return ret;
    }

    @Override
    public List<Fighter> getMinorFighters(String group) {
        FighterDAO fighterDao = new FighterDAO();
        List<Fighter> fList = fighterDao.getMinorCount();
        List<Fighter> retList = new ArrayList<>();
        for (Fighter f : fList) {
            if (f.getScaGroup().getGroupName().equals(group)) {
                retList.add(f);
            }
        }
        return retList;
    }

    @Override
    public void sendReportInfo(Map<String, Object> reportInfo) {
        log("send report");
        Queue queue = QueueFactory.getDefaultQueue();
        TaskOptions to = withUrl("/BuildReport.groovy");
        to.method(TaskOptions.Method.POST);
        for (String key : reportInfo.keySet()) {
            if (reportInfo.get(key) instanceof Collection) {
            } else {
                to.param(key, reportInfo.get(key).toString());
            }
        }
        to.header("Host", BackendServiceFactory.getBackendService().getBackendAddress("adminb"));
        queue.add(to);
    }

    @Override
    public List<Report> getAllReports() {
        String namespace = NamespaceManager.get();
        try {
            NamespaceManager.set("calontir");
            ReportDAO dao = new ReportDAO();
            return dao.select();
        } finally {
            NamespaceManager.set(namespace);
        }
    }

    @Override
    public void deleteReport(Report report) {
        String namespace = NamespaceManager.get();
        try {
            NamespaceManager.set("calontir");
            ReportDAO dao = new ReportDAO();
            dao.delete(report);
        } finally {
            NamespaceManager.set(namespace);
        }
    }

    @Override
    public List<Report> getReports(Integer days) {
        final Security security = SecurityFactory.getSecurity();
        log(String.format("Getting reports for the last %d days", days));
        List<Report> reports = null;
        log.log(Level.INFO, security.getUser().toString());
        if (security.isRoleOrGreater(UserRoles.CARD_MARSHAL)) {
            log.log(Level.INFO, "Card Marshal");
            final String namespace = NamespaceManager.get();
            try {
                NamespaceManager.set("calontir");
                ReportDAO dao = new ReportDAO();
                reports = dao.getForDays(days);
            } finally {
                NamespaceManager.set(namespace);
            }
        } else if (security.isRole(UserRoles.DEPUTY_EARL_MARSHAL)) {
            log.log(Level.INFO, "Deputy Earl Marshal");
            ScaGroupDAO groupDao = new ScaGroupDAO();
            List<String> groups = groupDao.getScaGroupNamesByRegion(security.getUser().getScaGroup().getGroupLocation());
            final String namespace = NamespaceManager.get();
            try {
                NamespaceManager.set("calontir");
                ReportDAO dao = new ReportDAO();
                reports = dao.getForRegionAndDays(days, groups);
            } finally {
                NamespaceManager.set(namespace);
            }
        } else {
            log.log(Level.INFO, "No reports allowed");
        }

        return reports;
    }

    @Override
    public Integer countFightersInGroup(String group) {
        final FighterDAO fighterDao = new FighterDAO();
        final ScaGroupDAO groupDao = new ScaGroupDAO();
        final ScaGroup scaGroup = groupDao.getScaGroupByName(group);
        return fighterDao.getFighterCountInGroup(scaGroup);
    }

    @Override
    public Integer countMinorsInGroup(String group) {
        final FighterDAO fighterDao = new FighterDAO();
        final ScaGroupDAO groupDao = new ScaGroupDAO();
        final ScaGroup scaGroup = groupDao.getScaGroupByName(group);
        return fighterDao.getMinorCountInGroup(scaGroup);
    }

}
