/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.dto;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author rik
 */
public class Treaty implements Serializable {
    private Long treatyId;
    private String name;
    private List<Fighter> fighters;

    public List<Fighter> getFighters() {
        return fighters;
    }

    public void setFighters(List<Fighter> fighters) {
        this.fighters = fighters;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTreatyId() {
        return treatyId;
    }

    public void setTreatyId(Long treatyId) {
        this.treatyId = treatyId;
    }
    
    
}
