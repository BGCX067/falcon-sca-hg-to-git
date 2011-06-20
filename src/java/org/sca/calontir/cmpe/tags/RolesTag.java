package org.sca.calontir.cmpe.tags;

import java.io.IOException;
import java.util.List;
import javax.servlet.jsp.JspWriter;
import org.sca.calontir.cmpe.common.UserRoles;
import org.sca.calontir.cmpe.dto.ScaGroup;
import org.sca.calontir.cmpe.db.ScaGroupDAO;

/**
 *
 * @author rik
 */
public class RolesTag extends CMPExtendedTagSupport {
    private UserRoles userRole;

    protected void doAdd(JspWriter out) throws IOException {
        out.println("<select name=\"userRole\">");
        for (UserRoles role : UserRoles.values()) {
            out.println("<option value=\"" + role.name() + "\">" + role.name() + "</option>");
        }
        out.println("</select>");
    }

    protected void doView(JspWriter out) throws IOException {
        if (userRole != null) {
            out.println("<input type=\"hidden\" name=\"userRole\" value=\"" + userRole + "\"/>");
            out.print(userRole);
        }
    }

    protected void doEdit(JspWriter out) throws IOException {
        out.println("<select name=\"userRole\">");
        for (UserRoles role : UserRoles.values()) {
            out.print("<option ");
            if (userRole != null) {
                if (role.equals(userRole)) {
                    out.print(" selected ");
                }
            }
            out.print(" value=\"" + role.name() + "\">" + role.name() + "</option>");
        }
        out.println("</select>");
    }

    public void setUserRole(UserRoles userRole) {
        this.userRole = userRole;
    }
}
