package org.sca.calontir.cmpe.tags;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

/**
 *
 * @author rik
 */
public class PrintButton extends CMPExtendedTagSupport {

    @Override
    public void doTag() throws JspException {
        JspWriter out = getJspContext().getOut();

        try {
            init();

            if (mode != null && mode.equals("add")) {
                doAdd(out);
            } else if (mode.startsWith("edit")) {
                doEdit(out);
            } else {
                doView(out);
            }
        } catch (java.io.IOException ex) {
            throw new JspException("Error in PrintButton tag", ex);
        }
    }

    @Override
    protected void doView(JspWriter out) throws IOException {
        out.print("<span class=\"printButton\">"
                + "<a href=\"#\" id=\"BPrint\" class=\"BPrint\""
                + " onClick=\"printThis(document.fighterInfoForm);\">Print</a>"
                + "</span>");
    }

    @Override
    protected void doEdit(JspWriter out) throws IOException {
        out.print("");
    }

    @Override
    protected void doAdd(JspWriter out) throws IOException {
        out.print("");
    }
}
