/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.tags;

import com.google.appengine.repackaged.com.google.common.base.StringUtil;
import java.io.IOException;
import java.util.List;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import org.sca.calontir.cmpe.data.Address;

/**
 *
 * @author rik
 */
public class AddressTag extends SimpleTagSupport {

    private String mode;
    private List addresses;

    /**
     * Called by the container to invoke this tag. 
     * The implementation of this method is provided by the tag library developer,
     * and handles all tag processing, body iteration, etc.
     */
    @Override
    public void doTag() throws JspException {
        JspWriter out = getJspContext().getOut();

        try {
            List<Address> addrs = (List<Address>) addresses;

            // change to for statement (bad hack, change)
            Address address = (addrs == null || addrs.isEmpty()) ? null : addrs.get(0);
            if (address == null) {
                address = new Address();
            }

            if (mode != null && mode.equals("add")) {
                doAdd(out, address);
            } else {
                doView(out, address);
            }
        } catch (java.io.IOException ex) {
            throw new JspException("Error in AuthorizationsTag tag", ex);
        }

    }
    
    private void doView(JspWriter out, Address address) throws IOException {
        out.print("Address:");
        out.print(address.getAddress1());
        if(address.getAddress2() != null && !StringUtil.isEmptyOrWhitespace(address.getAddress2())) {
            out.print(", ");
            out.print(address.getAddress2());
        }
        out.print(", " + address.getCity());
        out.print(", " + address.getState());
        out.print("&nbsp;&nbsp;" + address.getPostalCode());
    }

    private void doAdd(JspWriter out, Address address) throws IOException {
        out.print("Street:");
        out.print("<input type=\"text\" name=\"address1\"");
        out.print(" value=\"" + address.getAddress1() + "\"");
        out.print(" size=\"60\"");
        out.print(" onfocus=\"if (this.value==this.defaultValue) this.value='';"
                + " else this.select()\" onblur=\"if (!this.value) this.value=this.defaultValue)\"");
        out.print(" />");
        out.println("<br>");
        
        out.print("Line 2:");
        out.print("<input type=\"text\" name=\"address2\"");
        out.print(" value=\"" + address.getAddress2() + "\"");
        out.print(" size=\"60\"");
        out.print(" onfocus=\"if (this.value==this.defaultValue) this.value='';"
                + " else this.select()\" onblur=\"if (!this.value) this.value=this.defaultValue)\"");
        out.print(" />");
        out.println("<br>");
        
        out.print("City");
        out.print("<input type=\"text\" name=\"city\"");
        out.print(" value=\"" + address.getCity() + "\"");
        out.print(" size=\"20\"");
        out.print(" onfocus=\"if (this.value==this.defaultValue) this.value='';"
                + " else this.select()\" onblur=\"if (!this.value) this.value=this.defaultValue)\"");
        out.print(" />");
        out.println("<br>");
        
//        out.print("<input type=\"text\" name=\"district\"");
//        out.print(" value=\"" + address.getDistrict() + "\"");
//        out.print(" size=\"20\"");
//        out.print(" onfocus=\"if (this.value==this.defaultValue) this.value='';"
//                + " else this.select()\" onblur=\"if (!this.value) this.value=this.defaultValue)\"");
//        out.println(" />");
        
        out.print("Postal Code");
        out.print("<input type=\"text\" name=\"postalCode\"");
        out.print(" value=\"" + address.getPostalCode() + "\"");
        out.print(" size=\"60\"");
        out.print(" onfocus=\"if (this.value==this.defaultValue) this.value='';"
                + " else this.select()\" onblur=\"if (!this.value) this.value=this.defaultValue)\"");
        out.print(" />");
        out.println("<br>");
        
        out.print("State");
        out.print("<input type=\"text\" name=\"state\"");
        out.print(" value=\"" + address.getState() + "\"");
        out.print(" size=\"60\"");
        out.print(" onfocus=\"if (this.value==this.defaultValue) this.value='';"
                + " else this.select()\" onblur=\"if (!this.value) this.value=this.defaultValue)\"");
        out.print(" />");
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setAddresses(List addresses) {
        this.addresses = addresses;
    }
}
