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

	private static LookupController _instance = new LookupController();
	private List<AuthType> authTypes = null;
	private List<ScaGroup> scaGroups = null;
	private Map<Long, FighterInfo> fighterMap = null;
	private Long dateSaved = null;
	boolean dirty = false;

	private LookupController() {
		try {
			buildTables();
		} catch (Exception e) {
			Window.alert(e.toString());
		}
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
		buildTables();
		//fighterMap.put(replacement.getScaName(), replacement);
		//dirty = true;
		//writeDataToLocal();
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
						if (searchName == null || searchName.isEmpty()
								|| scaName.stringValue().toUpperCase().contains(searchName.toUpperCase())) {
							FighterInfo fli = new FighterInfo();
							fli.setFighterId(new Double(id.doubleValue()).longValue());
							fli.setScaName(scaName.stringValue());
							fli.setAuthorizations(auths.stringValue());
							fli.setGroup(group.stringValue());
							fighterMap.put(fli.getFighterId(), fli);
						}
					}
				} catch (Exception e) {
					Window.alert(e.getMessage());
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
					JSONObject scaNameObj = new JSONObject();
					scaNameObj.put("scaName", scaName);
					scaNameObj.put("id", id);
					scaNameObj.put("authorizations", auths);
					scaNameObj.put("group", group);
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
				stockStore.setItem("scaNameUpdated", Long.toString(now.getTime()));
			}
			dirty = false;
		}
	}

	private void getStoredList(final FighterServiceAsync fighterService) {
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, "/RehydrateDatabase.groovy");
		try {
			requestBuilder.sendRequest("request", new RequestCallback() {

				@Override
				public void onResponseReceived(Request request, Response response) {
					String text = response.getText();
					Storage stockStore = Storage.getLocalStorageIfSupported();
					if (stockStore != null) {
						stockStore.removeItem("scaNameList");
						stockStore.setItem("scaNameList", text);

						JSONValue value = JSONParser.parseStrict(text);
						JSONObject valueObj = value.isObject();
						//vv
						//JSONString saveDate = valueObj.get("dateSaved").isString();
						JSONNumber saveDate = valueObj.get("dateSaved").isNumber();
						double d = saveDate.doubleValue();
						long timeStamp = (new Double(d)).longValue();
						stockStore.setItem("scaNameUpdated", Long.toString(timeStamp));
						dateSaved = new Long(timeStamp);
						Date saved = new Date(timeStamp);

						getFighterList(null);
						getListItems(fighterService, saved);
					}
				}

				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert("Failed lookup " + exception.getMessage());
				}
			});
		} catch (RequestException ex) {
			Logger.getLogger(LookupController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void getListItems(final FighterServiceAsync fighterService, final Date targetDate) {
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
		Storage stockStore = Storage.getLocalStorageIfSupported();
		int i = 0;
		String timeStampStr = null;
		String scaNameListStr = null;
		if (stockStore != null) {
			timeStampStr = stockStore.getItem("scaNameUpdated");
			scaNameListStr = stockStore.getItem("scaNameList");
		}
		Date targetDate;
		if (timeStampStr == null || timeStampStr.trim().isEmpty()) {
			targetDate = null;
		} else {
			final long longDay = 86400000L;
			long timeStamp = Long.valueOf(timeStampStr);
			//long threeDays = 86400000L * 3;
			long now = new Date().getTime();
			if (now - longDay > timeStamp) {
				targetDate = null;
			} else {
				targetDate = new Date(timeStamp);
			}
//			if (scaNameListStr != null) {
//				JSONValue value = JSONParser.parseStrict(scaNameListStr);
//				JSONObject scaNameObjs = value.isObject();
//				JSONValue dateSavedVal = scaNameObjs.get("dateSaved");
//				JSONNumber dateSavedObj = null;
//				if (dateSavedVal != null) {
//					dateSavedObj = dateSavedVal.isNumber();
//					dateSaved = new Double(dateSavedObj.doubleValue()).longValue();
//					if (dateSaved > timeStamp) {
//						Window.alert("timeStamp " + timeStamp + ": dateSaved " + dateSaved);
//						targetDate = null;
//					}
//				}
//			}
		}

		try {
			getFighterList("");
		} catch (Exception e) {
		}

		FighterServiceAsync fighterService = GWT.create(FighterService.class);
		if (targetDate == null) {
			getStoredList(fighterService);
		} else {
			getListItems(fighterService, targetDate);
		}


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
