package org.sca.calontir.cmpe.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.joda.time.DateTime;
import org.sca.calontir.cmpe.client.FighterInfo;
import org.sca.calontir.cmpe.client.FighterListInfo;
import org.sca.calontir.cmpe.client.FighterService;
import org.sca.calontir.cmpe.client.StoredFighterList;
import org.sca.calontir.cmpe.db.AuthTypeDAO;
import org.sca.calontir.cmpe.db.FighterDAO;
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

			System.out.println("Getting List Items from DAO");
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
				retValList.add(info);
			}
		}
		retval.setFighterInfo(retValList);

		return retval;
	}

	@Override
	public StoredFighterList getStoredList() {
//		System.out.println("in getStoredList");
//		CompilerConfiguration compiler =new CompilerConfiguration();
//		compiler.setScriptBaseClass("Storage");
//		GroovyShell shell = new GroovyShell(compiler);
//		Map retMap = (Map) shell.evaluate("getFighterList()");
//		System.out.println("Got map, size: " + retMap.size());
//		//Storage storage = new Storage();
//		//Map retMap = storage.getFighterList();
//		StoredFighterList sfl = new StoredFighterList();
//
//		List<FighterListItem> fighters = (List<FighterListItem>) retMap.get("fighterList");
//		Date saved = (Date) retMap.get("saveDate");
//
//		sfl.setDateSaved(saved);
//
//		List<FighterInfo> retValList = new ArrayList<FighterInfo>();
//		for (FighterListItem fli : fighters) {
//			if (fli != null) {
//				FighterInfo info = new FighterInfo();
//				info.setFighterId(fli.getFighterId() == null ? 0 : fli.getFighterId());
//				info.setScaName(fli.getScaName() == null ? "" : fli.getScaName());
//				info.setAuthorizations(fli.getAuthorizations() == null ? "" : fli.getAuthorizations());
//				info.setGroup(fli.getGroup() == null ? "" : fli.getGroup());
//				retValList.add(info);
//			}
//		}
//		sfl.setFighterInfo(retValList);
//
//
//		return sfl;
		return null;
	}

	@Override
	public Fighter getFighter(Long id) {
		FighterDAO fighterDao = new FighterDAO();

		return fighterDao.getFighter(id);
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
		if(fList != null && !fList.isEmpty())
			return fList.get(0);
		return null;
	}
}
