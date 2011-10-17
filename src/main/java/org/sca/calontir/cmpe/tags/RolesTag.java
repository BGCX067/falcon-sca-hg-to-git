package org.sca.calontir.cmpe.tags;

import java.io.IOException;
import javax.servlet.jsp.JspWriter;
import org.sca.calontir.cmpe.common.UserRoles;

/**
 *
 * @author rik
 */
public class RolesTag extends CMPExtendedTagSupport {
    private UserRoles userRole;

    @Override
    protected void doAdd(JspWriter out) throws IOException {
        out.println("<select name=\"userRole\">");
        for (UserRoles role : UserRoles.values()) {
            out.println("<option value=\"" + role.name() + "\">" + role.name() + "</option>");
        }
        out.println("</select>");
    }

    @Override
    protected void doView(JspWriter out) throws IOException {
        if (userRole != null) {
            out.println("<input type=\"hidden\" name=\"userRole\" value=\"" + userRole + "\"/>");
            out.print(userRole);
        }
    }

    @Override
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
