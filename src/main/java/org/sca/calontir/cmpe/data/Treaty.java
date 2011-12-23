package org.sca.calontir.cmpe.data;

import com.google.appengine.api.datastore.Key;
import java.util.List;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 *
 * @author rik
 */
@PersistenceCapable()
public class Treaty {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key treatyId;
    @Persistent
    private String name;
    @Persistent
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

    public Key getTreatyId() {
        return treatyId;
    }

    public void setTreatyId(Key treatyId) {
        this.treatyId = treatyId;
    }
    
    
}
