package org.sca.calontir.cmpe.common;

import java.util.List;
import org.sca.calontir.cmpe.dto.ScaGroup;

/**
 *
 * @author rik
 */
public interface ScaGroupResource {

    void remove();

    List<ScaGroup> retrieve();

    void store(ScaGroup scaGroup);
    
}
