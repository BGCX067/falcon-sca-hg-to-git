package org.sca.calontir.cmpe.tags;

import java.io.IOException;
import javax.servlet.jsp.JspWriter;
import org.sca.calontir.cmpe.common.FighterStatus;

/**
 *
 * @author rik
 */
public class FighterStatusTag extends CMPExtendedTagSupport {

    private FighterStatus status;

    @Override
    protected void doView(JspWriter out) throws IOException {
        out.print(status.toString());
    }

    @Override
    protected void doEdit(JspWriter out) throws IOException {
        out.println("<select name=\"fighterStatus\">");
        for (FighterStatus f_status : FighterStatus.values()) {
            out.print("<option ");
            if (f_status != null) {
                if (f_status.equals(status)) {
                    out.print(" selected ");
                }
            }
            out.print(" value=\"" + f_status.toString() + "\">" + f_status.toString() + "</option>");
        }
        out.println("</select>");
    }

    @Override
    protected void doAdd(JspWriter out) throws IOException {
        out.println("<select name=\"fighterStatus\">");
        for (FighterStatus status : FighterStatus.values()) {
            out.println("<option value=\"" + status.toString() + "\">" + status.toString() + "</option>");
        }
        out.println("</select>");
    }

    public void setStatus(FighterStatus status) {
        this.status = status;
    }
}
