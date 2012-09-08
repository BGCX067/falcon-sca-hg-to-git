package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.*;
import com.google.gwt.json.client.*;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.sca.calontir.cmpe.client.FighterInfo;
import org.sca.calontir.cmpe.client.FighterListInfo;
import org.sca.calontir.cmpe.client.FighterService;
import org.sca.calontir.cmpe.client.FighterServiceAsync;
import org.sca.calontir.cmpe.dto.AuthType;
import org.sca.calontir.cmpe.dto.ScaGroup;

/**
 * Gets the data from local storage and the server.
 *
 * @author Rik Scarborough
 */
public class LookupController {

	private static final Logger log = Logger.getLogger(LookupController.class.getName());
	private final static long DAY = 86400000L;
	private static LookupController _instance = new LookupController();
	private List<AuthType> authTypes = null;
	private List<ScaGroup> scaGroups = null;
	private Map<Long, FighterInfo> fighterMap = new HashMap<Long, FighterInfo>();
	private Long dateSaved = null;
	boolean dirty = false;
	private boolean fighterDLComplete = false;
	private FighterServiceAsync fighterService = GWT.create(FighterService.class);

	private LookupController() {
		try {
			buildTables();
		} catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	public static LookupController getInstance() {
		return _instance;
	}

	public boolean isDLComplete() {
		return authTypes != null && scaGroups != null && fighterDLComplete;
	}

	public List<AuthType> getAuthType() {
		return authTypes;
	}

	public List<ScaGroup> getScaGroups() {
		return scaGroups;
	}

	public ScaGroup getScaGroup(String name) {
		for (ScaGroup group : scaGroups) {
			if (name.equals(group.getGroupName())) {
				return group;
			}
		}
		return null;
	}

	public void replaceFighter(FighterInfo replacement) {
		updateLocalData();
	}

	public List<FighterInfo> getFighterList(String searchName) {
		Storage stockStore;
		stockStore = Storage.getLocalStorageIfSupported();
		//if (!dirty) {
		fighterMap = new HashMap<Long, FighterInfo>();
		if (stockStore != null) {
			String scaNameListStr = stockStore.getItem("scaNameList");
			if (scaNameListStr != null && !scaNameListStr.trim().isEmpty()) {
				try {
					JSONValue value = JSONParser.parseStrict(scaNameListStr);
					JSONObject scaNameObjs = value.isObject();
					JSONValue dateSavedVal = scaNameObjs.get("dateSaved");
					JSONNumber dateSavedObj = null;
					if (dateSavedVal != null) {
						dateSavedObj = dateSavedVal.isNumber();
					}
					JSONArray scaNameArray = scaNameObjs.get("scaNames").isArray();
					if (dateSavedObj != null) {
						dateSaved = new Double(dateSavedObj.doubleValue()).longValue();
					}

					for (int i = 0; i < scaNameArray.size(); ++i) {
						JSONObject scaNameObj = scaNameArray.get(i).isObject();
						JSONString scaName = scaNameObj.get("scaName").isString();
						JSONNumber id = scaNameObj.get("id").isNumber();
						JSONString auths = scaNameObj.get("authorizations").isString();
						JSONString group = scaNameObj.get("group").isString();
						JSONString status = scaNameObj.get("status").isString();
						if (searchName == null || searchName.isEmpty()
								|| scaName.stringValue().toUpperCase().contains(searchName.toUpperCase())) {
							FighterInfo fli = new FighterInfo();
							fli.setFighterId(new Double(id.doubleValue()).longValue());
							fli.setScaName(scaName.stringValue());
							fli.setAuthorizations(auths.stringValue());
							fli.setGroup(group.stringValue());
							fli.setStatus(status.stringValue());
							fighterMap.put(fli.getFighterId(), fli);
						}
					}
				} catch (Exception e) {
					log.log(Level.SEVERE, e.getMessage(), e);
				}
			}
		}
		//}
		List<FighterInfo> fighterList = new ArrayList<FighterInfo>(fighterMap.values());

		Collections.sort(fighterList, new Comparator<FighterInfo>() {
			@Override
			public int compare(FighterInfo l, FighterInfo r) {
				return l.getScaName().compareToIgnoreCase(r.getScaName());
			}
		});
		return fighterList;
	}

	private void writeDataToLocal() {
		Storage stockStore = Storage.getLocalStorageIfSupported();
		if (stockStore != null) {
			JSONArray scaNameObjs;
			scaNameObjs = new JSONArray();
			JSONObject scaNameList = new JSONObject();

			int i = 0;
			if (fighterMap != null && fighterMap.size() > 0) {
				List<FighterInfo> fighterList = new ArrayList<FighterInfo>(fighterMap.values());

				Collections.sort(fighterList, new Comparator<FighterInfo>() {
					@Override
					public int compare(FighterInfo l, FighterInfo r) {
						return l.getScaName().compareToIgnoreCase(r.getScaName());
					}
				});
				for (FighterInfo fli : fighterList) {
					JSONString scaName = new JSONString(fli.getScaName());
					JSONNumber id = new JSONNumber(fli.getFighterId());
					JSONString auths = new JSONString(fli.getAuthorizations());
					JSONString group = new JSONString(fli.getGroup());
					JSONString status = new JSONString(fli.getStatus());
					JSONObject scaNameObj = new JSONObject();
					scaNameObj.put("scaName", scaName);
					scaNameObj.put("id", id);
					scaNameObj.put("authorizations", auths);
					scaNameObj.put("group", group);
					scaNameObj.put("status", status);
					scaNameObjs.set(i++, scaNameObj);
				}

				if (dateSaved != null) {
					JSONNumber dateSavedObj = new JSONNumber(dateSaved);
					scaNameList.put("dateSaved", dateSavedObj);
				}
				scaNameList.put("scaNames", scaNameObjs);
				stockStore.removeItem("scaNameList");
				stockStore.setItem("scaNameList", scaNameList.toString());
				Date now = new Date();
				stockStore.setItem("scaNameUpdated", Long.toString(now.getTime() - DAY));
			}
			dirty = false;
		}
	}

	public void updateLocalData() {
		final Date targetDate = new Date(dateSaved);
		dateSaved = new Date().getTime();
		getFighterList(null);
		fighterService.getListItems(targetDate, new AsyncCallback<FighterListInfo>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Failed lookup " + caught.getMessage());
				//send signal we failed.
			}

			@Override
			public void onSuccess(FighterListInfo result) {
				if (result.isUpdateInfo()) {
					for (FighterInfo fi : result.getFighterInfo()) {
						fighterMap.put(fi.getFighterId(), fi);
					}
				} else {
					for (FighterInfo fi : result.getFighterInfo()) {
						fighterMap.put(fi.getFighterId(), fi);
					}
				}

				writeDataToLocal();
				//signal we have the data;
			}
		});
	}

	private void buildTables() {
		final Storage stockStore = Storage.getLocalStorageIfSupported();
		fighterService.initialLookup(new AsyncCallback<Map<String, Object>>() {
			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(Map<String, Object> result) {
				String text = (String) result.get("stored");
				if (stockStore != null) {
					stockStore.removeItem("scaNameList");
					stockStore.setItem("scaNameList", text);
					JSONValue value = JSONParser.parseStrict(text);
					JSONObject valueObj = value.isObject();
					JSONNumber saveDate = valueObj.get("dateSaved").isNumber();
					double d = saveDate.doubleValue();
					long timeStamp = (new Double(d)).longValue();
					dateSaved = new Long(timeStamp);
					stockStore.setItem("scaNameUpdated", saveDate.toString());
					fighterDLComplete = true;
				}

				authTypes = (List<AuthType>) result.get("authTypes");
				scaGroups = (List<ScaGroup>) result.get("groups");
			}
		});
	}
}
