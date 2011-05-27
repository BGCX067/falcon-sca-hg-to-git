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
import org.sca.calontir.cmpe.data.Phone;

/**
 *
 * @author rik
 */
public class PhoneTag extends SimpleTagSupport {

    private String mode;
    private List numbers;

    /**
     * Called by the container to invoke this tag. 
     * The implementation of this method is provided by the tag library developer,
     * and handles all tag processing, body iteration, etc.
     */
    @Override
    public void doTag() throws JspException {
        JspWriter out = getJspContext().getOut();

        try {
            List<Phone> numberList = (List<Phone>) numbers;
            Phone number = (numberList == null || numberList.isEmpty()) ? null : numberList.get(0);
            if (number == null) {
                number = new Phone();
            }        
                    
            if (mode != null && mode.equals("add")) {
                doAdd(out, number);
            } else {
                doView(out, number);
            }
        } catch (java.io.IOException ex) {
            throw new JspException("Error in PhoneTag tag", ex);
        }
    }
    
    

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setNumbers(List numbers) {
        this.numbers = numbers;
    }

    private void doView(JspWriter out, Phone number)  throws IOException {
        out.print(StringUtils.trimToEmpty(number.getPhoneNumber()));
    }

    private void doAdd(JspWriter out, Phone number)  throws IOException {
        out.print("<input type=\"text\" name=\"phoneNumber\"");
        out.print(" value=\"" + StringUtils.trimToEmpty(number.getPhoneNumber()) + "\"");
        out.print(" size=\"40\"");
        out.print(" onfocus=\"if (this.value==this.defaultValue) this.value='';"
                + " else this.select()\" onblur=\"if (!this.value) this.value=this.defaultValue)\"");
        out.print(" />");
        out.println("<br>");
    }
}
