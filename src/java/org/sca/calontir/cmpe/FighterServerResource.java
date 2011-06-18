package org.sca.calontir.cmpe;

import java.util.logging.Level;
import org.restlet.data.Form;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import org.sca.calontir.cmpe.common.FighterResource;
import org.sca.calontir.cmpe.dto.Fighter;
import java.util.ArrayList;
import java.util.List;
import org.restlet.security.User;
import org.sca.calontir.cmpe.db.FighterDAO;

/**
 *
 * @author rik
 */
public class FighterServerResource extends ServerResource implements FighterResource {

    FighterDAO dao = new FighterDAO();

    @Get
    public List<Fighter> retrieve() {
        User u = getRequest().getClientInfo().getUser();
        List<Fighter> retVal = null;

        String fk = (String) getRequestAttributes().get("fighterkey");
        if (fk != null) {
            int ifk = Integer.parseInt(fk);
            if (ifk > 0) {
                Fighter f = dao.getFighter(ifk);
                retVal = new ArrayList<Fighter>();
                retVal.add(f);
            }
        }
        if (retVal == null || retVal.isEmpty()) {
            retVal = dao.getFighters();
        }

        Form form = getQuery();
        String gkey = form.getValues("gkey");
        getLogger().log(Level.INFO, "==>{0}", gkey);

        return retVal;
    }

    @Put
    public void store(Fighter fighter) {
        dao.saveFighter(fighter);
    }

    @Delete
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
