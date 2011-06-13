/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.tags;

import java.io.IOException;
import java.util.List;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import org.sca.calontir.cmpe.data.ScaGroup;
import org.sca.calontir.cmpe.db.ScaGroupDAO;

/**
 *
 * @author rik
 */
public class GroupTag extends CMPExtendedTagSupport {

    private String mode;
    private Long groupId;
    private ScaGroupDAO groupDao = null;
    
    @Override
    protected void init() {
        groupDao = new ScaGroupDAO();
        
    }


    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    protected void doAdd(JspWriter out) throws IOException {
        List<ScaGroup> groups = groupDao.getScaGroup();

        out.println("<select name=\"scaGroup\">");
        for (ScaGroup group : groups) {
            out.println("<option value=\"" + group.getScaGroupId().getId() + "\">" + group.getGroupName() + "</option>");
        }
        out.println("</select>");
    }

    protected void doView(JspWriter out) throws IOException {
        if (groupId != null) {
            ScaGroup scaGroup = groupDao.getScaGroup(groupId.intValue());
            if (scaGroup != null) {
                out.println("<input type=\"hidden\" name=\"scaGroup\" value=\"" + scaGroup.getScaGroupId().getId() + "\"/>");
                out.print(scaGroup.getGroupName());
            }
        }
    }

    protected void doEdit(JspWriter out) throws IOException {
        List<ScaGroup> groups = groupDao.getScaGroup();

        out.println("<select name=\"scaGroup\">");
        for (ScaGroup group : groups) {
            out.print("<option ");
            if(groupId != null) {
                if(group.getScaGroupId().getId() == groupId.intValue()) {
                    out.print(" selected ");
                }
            }
            out.print(" value=\"" + group.getScaGroupId().getId() + "\">" + group.getGroupName() + "</option>");
        }
        out.println("</select>");
    }
}
