/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.common;

import java.util.List;
import org.sca.calontir.cmpe.data.ScaGroup;

/**
 *
 * @author rik
 */
public interface ScaGroupResource {

    void remove();

    List<ScaGroup> retrieve();

    void store(ScaGroup scaGroup);
    
}
