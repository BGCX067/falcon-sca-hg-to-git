/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe;

import java.util.ArrayList;
import java.util.List;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import org.sca.calontir.cmpe.common.AuthTypeResource;
import org.sca.calontir.cmpe.data.AuthType;
import org.sca.calontir.cmpe.db.AuthTypeDAO;

/**
 *
 * @author rik
 */
public class AuthTypeServerResource extends ServerResource implements AuthTypeResource {

    AuthTypeDAO dao = new AuthTypeDAO();
    
    @Get
    public List<AuthType> retrieve() {
        List<AuthType> retVal = null;

        String fk = (String) getRequestAttributes().get("authTypekey");
        if (fk != null) {
            int ifk = Integer.parseInt(fk);
            if (ifk > 0) {
                AuthType f = dao.getAuthType(ifk);
                retVal = new ArrayList<AuthType>();
                retVal.add(f);
            }
        }
        if (retVal == null || retVal.isEmpty()) {
            retVal = dao.getAuthType();
        }
        
        return retVal;
    }

    @Put
    public void store(AuthType authType) {
        dao.saveAuthType(authType);
    }

    @Delete
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
