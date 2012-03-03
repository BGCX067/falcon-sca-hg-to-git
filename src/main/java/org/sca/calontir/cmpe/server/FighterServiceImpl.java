package org.sca.calontir.cmpe.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.util.ArrayList;
import java.util.List;
import org.sca.calontir.cmpe.client.FighterListInfo;
import org.sca.calontir.cmpe.client.FighterService;
import org.sca.calontir.cmpe.db.FighterDAO;
import org.sca.calontir.cmpe.dto.FighterListItem;

public class FighterServiceImpl extends RemoteServiceServlet implements FighterService {

    @Override
    public List<FighterListInfo> getListItems() {
        FighterDAO fighterDao = new FighterDAO();
        List<FighterListItem> fighters = fighterDao.getFighterListItems();
        
        List<FighterListInfo> retValList = new ArrayList<FighterListInfo>();
        for(FighterListItem fli : fighters) {
            FighterListInfo info = new FighterListInfo();
            info.setScaName(fli.getScaName());
            retValList.add(info);
        }
        
        return retValList;
    }
}
