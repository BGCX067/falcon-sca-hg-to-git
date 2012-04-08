package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;
import org.sca.calontir.cmpe.client.FighterService;
import org.sca.calontir.cmpe.client.FighterServiceAsync;
import org.sca.calontir.cmpe.dto.AuthType;
import org.sca.calontir.cmpe.dto.ScaGroup;

/**
 *
 * @author rikscarborough
 */
public class LookupController {
    private static LookupController _instance = new LookupController();
    private List<AuthType> authTypes = null;
    private List<ScaGroup> scaGroups = null;
    
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
    
    
    private void buildTables() {
        FighterServiceAsync fighterService = GWT.create(FighterService.class);
        
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
