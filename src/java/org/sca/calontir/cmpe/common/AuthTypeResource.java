/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.common;

import java.util.List;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.sca.calontir.cmpe.data.AuthType;

/**
 *
 * @author rik
 */
public interface AuthTypeResource {
    @Get
    public List<AuthType> retrieve();

    @Put
    public void store(AuthType authType);

    @Delete
    public void remove();
}
