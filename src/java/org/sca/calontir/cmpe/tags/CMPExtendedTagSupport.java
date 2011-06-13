/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.tags;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 *
 * @author rik
 */
public abstract class CMPExtendedTagSupport extends SimpleTagSupport {
    protected String mode;
    protected String editMode = "";
    
    public void doTag() throws JspException {
        JspWriter out = getJspContext().getOut();

        try {                 
            init();
            
            if (mode != null && mode.equals("add")) {
                doAdd(out);
                } else if (mode.equals(editMode)) {
                doEdit(out);
            } else {
                doView(out);
            }
        } catch (java.io.IOException ex) {
            throw new JspException("Error in PhoneTag tag", ex);
        }
    }
    
    protected void init() {
        
    }
    
    abstract protected void doView(JspWriter out)  throws IOException;
    abstract protected void doEdit(JspWriter out)  throws IOException;
    abstract protected void doAdd(JspWriter out)  throws IOException;

    public void setEditMode(String editMode) {
        this.editMode = editMode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
    
}
