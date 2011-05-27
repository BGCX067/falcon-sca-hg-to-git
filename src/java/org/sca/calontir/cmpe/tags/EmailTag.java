/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.tags;

import java.io.IOException;
import java.util.List;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import org.apache.commons.lang.StringUtils;
import org.sca.calontir.cmpe.data.Email;

/**
 *
 * @author rik
 */
public class EmailTag extends SimpleTagSupport {
    private String mode;
    private List emails;

    /**
     * Called by the container to invoke this tag. 
     * The implementation of this method is provided by the tag library developer,
     * and handles all tag processing, body iteration, etc.
     */
    @Override
    public void doTag() throws JspException {
        JspWriter out = getJspContext().getOut();
        
        try {
            List<Email> emailList = (List<Email>) emails;
            Email email = (emailList == null || emailList.isEmpty()) ? null : emailList.get(0);
            if (email == null) {
                email = new Email();
            }        
                    
            if (mode != null && mode.equals("add")) {
                doAdd(out, email);
            } else {
                doView(out, email);
            }

        } catch (java.io.IOException ex) {
            throw new JspException("Error in EmailTag tag", ex);
        }
    }

    private void doView(JspWriter out, Email email)  throws IOException {
        out.print(StringUtils.trimToEmpty(email.getEmailAddress()));
    }

    private void doAdd(JspWriter out, Email email)  throws IOException {
        out.print("<input type=\"text\" name=\"email\"");
        out.print(" value=\"" + StringUtils.trimToEmpty(email.getEmailAddress()) + "\"");
        out.print(" size=\"40\"");
        out.print(" onfocus=\"if (this.value==this.defaultValue) this.value='';"
                + " else this.select()\" onblur=\"if (!this.value) this.value=this.defaultValue)\"");
        out.print(" />");
        out.println("<br>");
    }
    
    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setEmails(List emails) {
        this.emails = emails;
    }
}
