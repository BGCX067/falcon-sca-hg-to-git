package org.sca.calontir.cmpe.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.Date;
import java.util.List;
import org.sca.calontir.cmpe.dto.AuthType;
import org.sca.calontir.cmpe.dto.Fighter;
import org.sca.calontir.cmpe.dto.ScaGroup;

@RemoteServiceRelativePath("fighter")
public interface FighterService  extends RemoteService {
    public FighterListInfo getListItems(Date targetDate);

	public StoredFighterList getStoredList();
    
    public Fighter getFighter(Long id);

	public Fighter getFighterByScaName(String scaName);
    
    public List<AuthType> getAuthTypes();
    
    public List<ScaGroup> getGroups();
}
