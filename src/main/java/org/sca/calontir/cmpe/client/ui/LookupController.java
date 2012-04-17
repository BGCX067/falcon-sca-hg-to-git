package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.*;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.*;
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

	private static LookupController _instance = new LookupController();
	private List<AuthType> authTypes = null;
	private List<ScaGroup> scaGroups = null;
	private Map<String, FighterInfo> fighterMap = null;
	boolean dirty = false;

	private LookupController() {
		buildTables();
	}

	public static LookupController getInstance() {
		return _instance;
	}

	public List<AuthType> getAuthType() {
		return authTypes;
	}

	public List<ScaGroup> getScaGroups() {
		return scaGroups;
	}

	public void replaceFighter(FighterInfo replacement) {
		//buildTables();
		fighterMap.put(replacement.getScaName(), replacement);
		dirty = true;
		writeDataToLocal();
	}

	public List<FighterInfo> getFighterList(String searchName) {
		Storage stockStore;
		stockStore = Storage.getLocalStorageIfSupported();
		if (!dirty) {
			fighterMap = new HashMap<String, FighterInfo>();
			if (stockStore != null) {
				String scaNameListStr = stockStore.getItem("scaNameList");
				JSONValue value = JSONParser.parseStrict(scaNameListStr);
				JSONObject scaNameObjs = value.isObject();
				JSONArray scaNameArray = scaNameObjs.get("scaNames").isArray();

				try {
					for (int i = 0; i < scaNameArray.size() - 1; ++i) {
						JSONObject scaNameObj = scaNameArray.get(i).isObject();
						JSONString scaName = scaNameObj.get("scaName").isString();
						JSONNumber id = scaNameObj.get("id").isNumber();
						JSONString auths = scaNameObj.get("authorizations").isString();
						JSONString group = scaNameObj.get("group").isString();
						if (searchName == null || searchName.isEmpty()
								|| scaName.stringValue().toUpperCase().contains(searchName.toUpperCase())) {
							FighterInfo fli = new FighterInfo();
							fli.setFighterId(new Double(id.doubleValue()).longValue());
							fli.setScaName(scaName.stringValue());
							fli.setAuthorizations(auths.stringValue());
							fli.setGroup(group.stringValue());
							fighterMap.put(fli.getScaName(), fli);
						}
					}
				} catch (Exception e) {
					Window.alert(e.getMessage());
				}

			}
		}
		List<FighterInfo> fighterList = new ArrayList<FighterInfo>(fighterMap.values());

		Collections.sort(fighterList, new Comparator<FighterInfo>() {

			@Override
			public int compare(FighterInfo l, FighterInfo r) {
				return l.getScaName().compareTo(r.getScaName());
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
						return l.getScaName().compareTo(r.getScaName());
					}
				});
				for (FighterInfo fli : fighterList) {
					JSONString scaName = new JSONString(fli.getScaName());
					JSONNumber id = new JSONNumber(fli.getFighterId());
					JSONString auths = new JSONString(fli.getAuthorizations());
					JSONString group = new JSONString(fli.getGroup());
					JSONObject scaNameObj = new JSONObject();
					scaNameObj.put("scaName", scaName);
					scaNameObj.put("id", id);
					scaNameObj.put("authorizations", auths);
					scaNameObj.put("group", group);
					scaNameObjs.set(i++, scaNameObj);
				}

				scaNameList.put("scaNames", scaNameObjs);
				stockStore.removeItem("scaNameList");
				stockStore.setItem("scaNameList", scaNameList.toString());
				Date now = new Date();
				stockStore.setItem("scaNameUpdated", Long.toString(now.getTime()));
			}
			dirty = false;
		}
	}

	private void buildTables() {
		Storage stockStore = Storage.getLocalStorageIfSupported();
		String timeStampStr = null;
		if (stockStore != null) {
			timeStampStr = stockStore.getItem("scaNameUpdated");
		}
		Date targetDate;
		if (timeStampStr == null || timeStampStr.trim().isEmpty()) {
			targetDate = null;
		} else {
			long timeStamp = Long.valueOf(timeStampStr);
			targetDate = new Date(timeStamp);
		}

		getFighterList(null);

		FighterServiceAsync fighterService = GWT.create(FighterService.class);
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
						fighterMap.put(fi.getScaName(), fi);
					}
				} else {
					for (FighterInfo fi : result.getFighterInfo()) {
						fighterMap.put(fi.getScaName(), fi);
					}
				}

				writeDataToLocal();
				//signal we have the data;
			}
		});

		fighterService.getAuthTypes(new AsyncCallback<List<AuthType>>() {

			@Override
			public void onFailure(Throwable caught) {
				throw new UnsupportedOperationException("Not supported yet.");
			}

			@Override
			public void onSuccess(List<AuthType> result) {
				authTypes = result;
			}
		});

		fighterService.getGroups(new AsyncCallback<List<ScaGroup>>() {

			@Override
			public void onFailure(Throwable caught) {
				throw new UnsupportedOperationException("Not supported yet.");
			}

			@Override
			public void onSuccess(List<ScaGroup> result) {
				scaGroups = result;
			}
		});
	}
}
