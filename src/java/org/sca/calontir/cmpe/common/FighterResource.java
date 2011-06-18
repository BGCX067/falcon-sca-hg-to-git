package org.sca.calontir.cmpe.common;

import java.util.List;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

import org.sca.calontir.cmpe.dto.Fighter;

/**
 *
 * @author rik
 */
public interface FighterResource {
    @Get
    public List<Fighter> retrieve();

    @Put
    public void store(Fighter fighter);

    @Delete
    public void remove();
}
