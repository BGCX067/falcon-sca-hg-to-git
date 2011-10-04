package org.sca.calontir.cmpe.tags;

import java.io.IOException;
import java.util.List;
import javax.servlet.jsp.JspWriter;
import org.sca.calontir.cmpe.dto.ScaGroup;
import org.sca.calontir.cmpe.db.ScaGroupDAO;

/**
 *
 * @author rik
 */
public class GroupTag extends CMPExtendedTagSupport {

    private String mode;
    private String groupName;
    private ScaGroupDAO groupDao = null;

    @Override
    protected void init() {
        groupDao = new ScaGroupDAO();

    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    protected void doAdd(JspWriter out) throws IOException {
        List<ScaGroup> groups = groupDao.getScaGroup();

        out.println("<select name=\"scaGroup\">");
        for (ScaGroup group : groups) {
            out.println("<option value=\"" + group.getGroupName() + "\">" + group.getGroupName() + "</option>");
        }
        out.println("</select>");
    }

    protected void doView(JspWriter out) throws IOException {
        if (groupName != null) {
            out.println("<input type=\"hidden\" name=\"scaGroup\" value=\"" + groupName + "\"/>");
            out.print(groupName);
        }
    }

    protected void doEdit(JspWriter out) throws IOException {
        List<ScaGroup> groups = groupDao.getScaGroup();

        out.println("<select name=\"scaGroup\">");
        for (ScaGroup group : groups) {
            out.print("<option ");
            if (groupName != null && group.getGroupName() != null) {
                if (group.getGroupName().equals(groupName)) {
                    out.print(" selected ");
                }
            }
            out.print(" value=\"" + group.getGroupName() + "\">" + group.getGroupName() + "</option>");
        }
        out.println("</select>");
    }
}
