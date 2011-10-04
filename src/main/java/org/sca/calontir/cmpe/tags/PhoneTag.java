package org.sca.calontir.cmpe.tags;

import java.io.IOException;
import java.util.List;
import javax.servlet.jsp.JspWriter;
import org.apache.commons.lang.StringUtils;
import org.sca.calontir.cmpe.dto.Phone;

/**
 *
 * @author rik
 */
public class PhoneTag extends CMPExtendedTagSupport {

    private List numbers;
    // local
    private Phone number;

    @Override
    protected void init() {
        List<Phone> numberList = (List<Phone>) numbers;
            number = (numberList == null || numberList.isEmpty()) ? null : numberList.get(0);
            if (number == null) {
                number = new Phone();
            }
    }

    protected void doView(JspWriter out) throws IOException {
        out.print(StringUtils.trimToEmpty(number.getPhoneNumber()));
    }

    protected void doEdit(JspWriter out) throws IOException {
        out.print("<input type=\"text\" name=\"phoneNumber\"");
        out.print(" value=\"" + StringUtils.trimToEmpty(number.getPhoneNumber()) + "\"");
        out.print(" size=\"40\"");
        out.println("<br>");
    }

    protected void doAdd(JspWriter out) throws IOException {
        out.print("<input type=\"text\" name=\"phoneNumber\"");
        out.print(" value=\"" + StringUtils.trimToEmpty(number.getPhoneNumber()) + "\"");
        out.print(" size=\"40\"");
        out.print(" onfocus=\"if (this.value==this.defaultValue) this.value='';"
                + " else this.select()\" onblur=\"if (!this.value) this.value=this.defaultValue)\"");
        out.print(" />");
        out.println("<br>");
    }

    public void setNumbers(List numbers) {
        this.numbers = numbers;
    }
}
