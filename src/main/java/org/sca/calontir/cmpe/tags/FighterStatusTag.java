package org.sca.calontir.cmpe.tags;

import java.io.IOException;
import javax.servlet.jsp.JspWriter;
import org.sca.calontir.cmpe.common.FighterStatus;
import org.sca.calontir.cmpe.common.UserRoles;
import org.sca.calontir.cmpe.user.Security;
import org.sca.calontir.cmpe.user.SecurityFactory;

/**
 *
 * @author rik
 */
public class FighterStatusTag extends CMPExtendedTagSupport {

    private FighterStatus status;
    private Security security = SecurityFactory.getSecurity();

    @Override
    protected void doView(JspWriter out) throws IOException {
        if (security.isRoleOrGreater(UserRoles.USER)) {
            out.println("Status: ");
            out.print(status.toString());
        } else {
            out.println("");
        }
    }

    @Override
    protected void doEdit(JspWriter out) throws IOException {
        if (security.isRoleOrGreater(UserRoles.USER)) {
            out.println("Status: ");
            if (security.isRoleOrGreater(UserRoles.CARD_MARSHAL)) {
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
            } else {
                doView(out);
            }
        } else {
            out.println("");
        }
    }

    @Override
    protected void doAdd(JspWriter out) throws IOException {
        if (security.isRoleOrGreater(UserRoles.CARD_MARSHAL)) {
            out.println("Status: ");
            out.println("<select name=\"fighterStatus\">");
            for (FighterStatus s : FighterStatus.values()) {
                out.println("<option value=\"" + s.toString() + "\">" + s.toString() + "</option>");
            }
            out.println("</select>");
        } else {
            out.println("");
        }
    }

    public void setStatus(FighterStatus status) {
        this.status = status;
    }
}
