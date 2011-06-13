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
public class GroupTag extends SimpleTagSupport {

    private String mode;
    private Long groupId;
    private ScaGroupDAO groupDao = null;

    /**
     * Called by the container to invoke this tag. 
     * The implementation of this method is provided by the tag library developer,
     * and handles all tag processing, body iteration, etc.
     */
    @Override
    public void doTag() throws JspException {
        JspWriter out = getJspContext().getOut();
        groupDao = new ScaGroupDAO();

        try {
            if (mode != null && mode.equals("add")) {
                doAdd(out);
            } else {
                doView(out);
            }

        } catch (java.io.IOException ex) {
            throw new JspException("Error in GroupTag tag", ex);
        }
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    private void doAdd(JspWriter out) throws IOException {
        List<ScaGroup> groups = groupDao.getScaGroup();

        out.println("<select name=\"scaGroup\">");
        for (ScaGroup group : groups) {
            out.println("<option value=\"" + group.getScaGroupId().getId() + "\">" + group.getGroupName() + "</option>");
        }
        out.println("</select>");
    }

    private void doView(JspWriter out) throws IOException {
        if (groupId != null) {
            ScaGroup scaGroup = groupDao.getScaGroup(groupId.intValue());
            if (scaGroup != null) {
                out.println("<input type=\"hidden\" name=\"scaGroup\" value=\"" + scaGroup.getScaGroupId().getId() + "\"/>");
                out.print(scaGroup.getGroupName());
            }
        }
    }
}
