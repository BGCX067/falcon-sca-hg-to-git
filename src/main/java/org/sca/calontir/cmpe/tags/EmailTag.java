package org.sca.calontir.cmpe.tags;

import java.io.IOException;
import java.util.List;
import javax.servlet.jsp.JspWriter;
import org.apache.commons.lang.StringUtils;
import org.sca.calontir.cmpe.dto.Email;

/**
 *
 * @author rik
 */
public class EmailTag extends CMPExtendedTagSupport {
    private List emails;
    
    // local
    private Email email;

    @Override
    protected void init() {
        List<Email> emailList = (List<Email>) emails;
            email = (emailList == null || emailList.isEmpty()) ? null : emailList.get(0);
            if (email == null) {
                email = new Email();
            }      
    }

    public void setEmails(List emails) {
        this.emails = emails;
    }

    @Override
    protected void doView(JspWriter out) throws IOException {
        out.print(StringUtils.trimToEmpty(email.getEmailAddress()));
    }

    @Override
    protected void doEdit(JspWriter out) throws IOException {
         out.print("<input type=\"text\" name=\"email\"");
        out.print(" value=\"" + StringUtils.trimToEmpty(email.getEmailAddress()) + "\"");
        out.print(" size=\"40\"");
        out.print(" />");
        out.println("<br>");
    }

    @Override
    protected void doAdd(JspWriter out) throws IOException {
         out.print("<input type=\"text\" name=\"email\"");
        out.print(" value=\"" + StringUtils.trimToEmpty(email.getEmailAddress()) + "\"");
        out.print(" size=\"40\"");
        out.print(" onfocus=\"if (this.value==this.defaultValue) this.value='';"
                + " else this.select()\" onblur=\"if (!this.value) this.value=this.defaultValue)\"");
        out.print(" />");
        out.println("<br>");
    }
}
