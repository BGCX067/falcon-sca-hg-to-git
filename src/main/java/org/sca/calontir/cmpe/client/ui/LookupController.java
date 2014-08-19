package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.sca.calontir.cmpe.client.FighterInfo;
import org.sca.calontir.cmpe.client.FighterService;
import org.sca.calontir.cmpe.client.FighterServiceAsync;
import org.sca.calontir.cmpe.dto.AuthType;
import org.sca.calontir.cmpe.dto.Report;
import org.sca.calontir.cmpe.dto.ScaGroup;

/**
 * Gets the data from local storage and the server.
 *
 * @author Rik Scarborough
 */
public class LookupController {

    private static final Logger log = Logger.getLogger(LookupController.class.getName());
    private final static long DAY = 86400000L;
    private static final LookupController _instance = new LookupController();
    private List<AuthType> authTypes = null;
    private List<ScaGroup> scaGroups = null;
    private Map<Long, FighterInfo> fighterMap = new HashMap<Long, FighterInfo>();
    boolean dirty = false;
    private boolean fighterDLComplete = false;
    private FighterServiceAsync fighterService = GWT.create(FighterService.class);
    private Storage stockStore = Storage.getLocalStorageIfSupported();
    public String versionId;

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

    public FighterInfo getFighter(Long id) {
        return fighterMap.get(id);
    }

    public void replaceFighter(FighterInfo replacement) {
    }

    public List<FighterInfo> getFighterList(String searchName) {
        fighterMap = new HashMap<Long, FighterInfo>();
        List<FighterInfo> fighterList = new ArrayList<FighterInfo>(fighterMap.values());

        return fighterList;
    }

    private void buildTables() {
        fighterService.initialLookup(new AsyncCallback<Map<String, Object>>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(Map<String, Object> result) {
                versionId = (String) result.get("appversion");
                authTypes = (List<AuthType>) result.get("authTypes");
                scaGroups = (List<ScaGroup>) result.get("groups");
                fighterDLComplete = true;
            }
        });
    }

    public void retrieveReports() {
        fighterService.getAllReports(new AsyncCallback<List<Report>>() {
            @Override
            public void onFailure(Throwable caught) {
                log.severe("getAllReports " + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Report> result) {
                String s = "";
                for (Report r : result) {
                    s += r.getMarshalName() + "<br";
                    for (String k : r.getReportParams().keySet()) {
                        s += k + ":" + r.getReportParams().get(k) + "<br>";
                    }
                }
                Window.alert(s);
            }
        });
    }
}
