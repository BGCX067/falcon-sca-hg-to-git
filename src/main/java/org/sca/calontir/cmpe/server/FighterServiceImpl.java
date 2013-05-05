package org.sca.calontir.cmpe.server;

import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.backends.BackendServiceFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreInputStream;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
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
import org.sca.calontir.cmpe.client.FighterService;
import org.sca.calontir.cmpe.db.AuthTypeDAO;
import org.sca.calontir.cmpe.db.FighterDAO;
import org.sca.calontir.cmpe.db.ReportDAO;
import org.sca.calontir.cmpe.db.ScaGroupDAO;
import org.sca.calontir.cmpe.db.TableUpdatesDao;
import org.sca.calontir.cmpe.dto.*;

public class FighterServiceImpl extends RemoteServiceServlet implements FighterService {

	@Override
	public FighterListInfo getListItems(Date targetDate) {
		FighterListInfo retval = new FighterListInfo();
		FighterDAO fighterDao = new FighterDAO();
		TableUpdatesDao tuDao = new TableUpdatesDao();
		TableUpdates tu = tuDao.getTableUpdates("Fighter");
		List<FighterListItem> fighters;
		if (targetDate == null
				|| (tu != null
				&& tu.getLastDeletion() != null
				&& new DateTime(tu.getLastDeletion()).isAfter(new DateTime(targetDate)))) {

			fighters = fighterDao.getFighterListItems();
			retval.setUpdateInfo(false);
		} else {
			fighters = fighterDao.getFighterListItems(new DateTime(targetDate));
			retval.setUpdateInfo(true);
		}

		List<FighterInfo> retValList = new ArrayList<FighterInfo>();
		for (FighterListItem fli : fighters) {
			if (fli != null) {
				FighterInfo info = new FighterInfo();
				info.setFighterId(fli.getFighterId() == null ? 0 : fli.getFighterId());
				info.setScaName(fli.getScaName() == null ? "" : fli.getScaName());
				info.setAuthorizations(fli.getAuthorizations() == null ? "" : fli.getAuthorizations());
				info.setGroup(fli.getGroup() == null ? "" : fli.getGroup());
				info.setStatus(fli.getStatus() == null ? "" : fli.getStatus().toString());
				info.setMinor(fli.isMinor());
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
	public Long saveFighter(Fighter fighter) {
		FighterDAO fighterDao = new FighterDAO();
		try {
			return fighterDao.saveFighter(fighter, fighter.getFighterId(), false);
		} catch (ValidationException ex) {
			Logger.getLogger(FighterServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
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
		Logger.getLogger(FighterServiceImpl.class.getName()).log(Level.INFO, "Start Initial Lookup");
		Map<String, Object> iMap = new HashMap<String, Object>();
		// get application version
		iMap.put("appversion", "1.1.13");

		// get from blob
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		String namespace = NamespaceManager.get();
		String blobKeyStr;
		try {
			NamespaceManager.set("system");

			String name = "calontir.snapshotkey";
			Query query = new Query("properties");
			query.setFilter(new Query.FilterPredicate("name", Query.FilterOperator.EQUAL, name));
			PreparedQuery preparedQuery = datastore.prepare(query);
			Entity entity = preparedQuery.asSingleEntity();
			blobKeyStr = (String) entity.getProperty("property");
		} finally {
			NamespaceManager.set(namespace);
		}
		BlobKey blobKey = new BlobKey(blobKeyStr);
		BlobstoreService blobStoreService = BlobstoreServiceFactory.getBlobstoreService();
		try {
			BlobstoreInputStream bis = new BlobstoreInputStream(blobKey);
			BufferedReader reader = new BufferedReader(new InputStreamReader(bis));
			StringWriter sw = new StringWriter();
			char[] buffer = new char[1024 * 4];
			int n = 0;
			while (-1 != (n = reader.read(buffer))) {
				sw.write(buffer, 0, n);
			}
			String text = sw.toString();

			iMap.put("stored", text);
		} catch (IOException ex) {
			Logger.getLogger(FighterServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
		}

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
		List<Fighter> retList = new ArrayList<Fighter>();
		for (Fighter f : fList) {
			if (f.getScaGroup().getGroupName().equals(group)) {
				retList.add(f);
			}
		}
		return retList;
	}

	@Override
	public void sendReportInfo(Map<String, Object> reportInfo) {
		FighterDAO fighterDao = new FighterDAO();
		Fighter user = fighterDao.getFighterByGoogleId((String) reportInfo.get("user.googleid"));

		if (user != null) {
			String membershipExpires = (String) reportInfo.get("Membership Expires");
			if (!membershipExpires.equals(user.getMembershipExpires())) {
				user.setMembershipExpires(membershipExpires);
				try {
					fighterDao.saveFighter(user, user.getFighterId(), false);
				} catch (ValidationException ex) {
					Logger.getLogger(FighterServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}


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

}
