package org.sca.calontir.cmpe.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.joda.time.DateTime;
import org.sca.calontir.cmpe.client.FighterInfo;
import org.sca.calontir.cmpe.client.FighterListInfo;
import org.sca.calontir.cmpe.client.FighterService;
import org.sca.calontir.cmpe.data.TableUpdates;
import org.sca.calontir.cmpe.db.FighterDAO;
import org.sca.calontir.cmpe.db.TableUpdatesDao;
import org.sca.calontir.cmpe.dto.Fighter;
import org.sca.calontir.cmpe.dto.FighterListItem;

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
    
    public Fighter getFighter(Long id) {
        FighterDAO fighterDao = new FighterDAO();
        
        return fighterDao.getFighter(id);
    }
}
