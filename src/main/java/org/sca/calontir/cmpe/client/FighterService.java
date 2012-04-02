package org.sca.calontir.cmpe.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.Date;
import org.sca.calontir.cmpe.dto.Fighter;

@RemoteServiceRelativePath("fighter")
public interface FighterService  extends RemoteService {
    public FighterListInfo getListItems(Date targetDate);
    
    public Fighter getFighter(Long id);
}
