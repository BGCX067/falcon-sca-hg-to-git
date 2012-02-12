package org.sca.calontir.cmpe.common;

import java.util.List;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.sca.calontir.cmpe.dto.ScaGroup;

/**
 *
 * @author rik
 */
public interface ScaGroupResource {

    @Delete
    void remove();

    @Get
    List<ScaGroup> retrieve();

    @Put
    void store(ScaGroup scaGroup);
    
}
