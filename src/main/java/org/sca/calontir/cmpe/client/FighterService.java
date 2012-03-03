package org.sca.calontir.cmpe.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;

@RemoteServiceRelativePath("fighter")
public interface FighterService  extends RemoteService {
    public List<FighterListInfo> getListItems();
}
