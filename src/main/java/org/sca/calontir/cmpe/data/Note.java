package org.sca.calontir.cmpe.data;

import com.google.appengine.api.datastore.Key;
import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 *
 * @author rik
 */
@PersistenceCapable(detachable = "true")
public class Note {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key noteId;
    @Persistent
    private String body;
    @Persistent
    private Date updated;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Key getNoteId() {
        return noteId;
    }

    public void setNoteId(Key noteId) {
        this.noteId = noteId;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
    
    
}
