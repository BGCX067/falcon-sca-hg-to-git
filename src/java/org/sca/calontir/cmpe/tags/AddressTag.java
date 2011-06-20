package org.sca.calontir.cmpe.tags;

import java.io.IOException;
import java.util.List;
import javax.servlet.jsp.JspWriter;
import org.apache.commons.lang.StringUtils;
import org.sca.calontir.cmpe.dto.Address;

/**
 *
 * @author rik
 */
public class AddressTag extends CMPExtendedTagSupport {

    private List addresses;
    //local
    private Address address;

    @Override
    protected void init() {
        List<Address> addrs = (List<Address>) addresses;

        // change to for statement (bad hack, change)
        address = (addrs == null || addrs.isEmpty()) ? null : addrs.get(0);
        if (address == null) {
            address = new Address();
        }

    }

    protected void doView(JspWriter out) throws IOException {
        out.print(StringUtils.trimToEmpty(address.getAddress1()));
        if (StringUtils.isNotBlank(address.getAddress2())) {
            out.print(", ");
            out.print(address.getAddress2());
        }
        out.print(", " + StringUtils.trimToEmpty(address.getCity()));
        out.print(", " + StringUtils.trimToEmpty(address.getState()));
        out.print("&nbsp;&nbsp;" + StringUtils.trimToEmpty(address.getPostalCode()));
    }

    protected void doAdd(JspWriter out) throws IOException {
        writeInputs(out, true);
    }

    @Override
    protected void doEdit(JspWriter out) throws IOException {
        writeInputs(out, false);
    }

    private void writeInputs(JspWriter out, boolean add) throws IOException {
        out.println("<table>");
        out.println("<tr>");
        out.print("<td>Street:</td>");
        out.print("<td><input type=\"text\" name=\"address1\"");
        out.print(" value=\"" + StringUtils.trimToEmpty(address.getAddress1()) + "\"");
        out.print(" size=\"60\"");
        if (add) {
            out.print(" onfocus=\"if (this.value==this.defaultValue) this.value='';"
                    + " else this.select()\" onblur=\"if (!this.value) this.value=this.defaultValue)\"");
        }
        out.print(" />");
        out.println("</td>");
        out.println("</tr>");

        out.println("<tr>");
        out.print("<td>Line 2:</td>");
        out.print("<td><input type=\"text\" name=\"address2\"");
        out.print(" value=\"" + StringUtils.trimToEmpty(address.getAddress2()) + "\"");
        out.print(" size=\"60\"");
        if (add) {
            out.print(" onfocus=\"if (this.value==this.defaultValue) this.value='';"
                    + " else this.select()\" onblur=\"if (!this.value) this.value=this.defaultValue)\"");
        }
        out.print(" />");
        out.println("</td>");
        out.println("</tr>");

        out.println("<tr>");
        out.print("<td>City:</td>");
        out.print("<td><input type=\"text\" name=\"city\"");
        out.print(" value=\"" + StringUtils.trimToEmpty(address.getCity()) + "\"");
        out.print(" size=\"20\"");
        if (add) {
            out.print(" onfocus=\"if (this.value==this.defaultValue) this.value='';"
                    + " else this.select()\" onblur=\"if (!this.value) this.value=this.defaultValue)\"");
        }
        out.print(" />");
        out.println("</td>");
        out.println("</tr>");

//        out.print("<input type=\"text\" name=\"district\"");
//        out.print(" value=\"" + address.getDistrict() + "\"");
//        out.print(" size=\"20\"");
//        out.print(" onfocus=\"if (this.value==this.defaultValue) this.value='';"
//                + " else this.select()\" onblur=\"if (!this.value) this.value=this.defaultValue)\"");
//        out.println(" />");


        out.println("<tr>");
        out.print("<td>State:</td>");
        out.print("<td><input type=\"text\" name=\"state\"");
        out.print(" value=\"" + StringUtils.trimToEmpty(address.getState()) + "\"");
        out.print(" size=\"20\"");
        if (add) {
            out.print(" onfocus=\"if (this.value==this.defaultValue) this.value='';"
                    + " else this.select()\" onblur=\"if (!this.value) this.value=this.defaultValue)\"");
        }
        out.print(" />");
        out.println("</td>");
        out.println("</tr>");

        out.println("<tr>");
        out.print("<td>Postal Code:</td>");
        out.print("<td><input type=\"text\" name=\"postalCode\"");
        out.print(" value=\"" + StringUtils.trimToEmpty(address.getPostalCode()) + "\"");
        out.print(" size=\"30\"");
        if (add) {
            out.print(" onfocus=\"if (this.value==this.defaultValue) this.value='';"
                    + " else this.select()\" onblur=\"if (!this.value) this.value=this.defaultValue)\"");
        }
        out.print(" />");
        out.println("</td>");
        out.println("</tr>");
        out.println("</table>");

    }

    public void setAddresses(List addresses) {
        this.addresses = addresses;
    }
}
