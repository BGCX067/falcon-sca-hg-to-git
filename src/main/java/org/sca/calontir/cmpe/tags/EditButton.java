package org.sca.calontir.cmpe.tags;

import java.io.IOException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 *
 * @author rik
 */
public class EditButton extends SimpleTagSupport {

    private String mode;
    private String target;
    private String form;

    /**
     * Called by the container to invoke this tag. 
     * The implementation of this method is provided by the tag library developer,
     * and handles all tag processing, body iteration, etc.
     */
    @Override
    public void doTag() throws JspException {
        JspWriter out = getJspContext().getOut();

        try {
            if (mode != null && mode.equals("add")) {
                doAdd(out);
            } else if (mode != null && mode.startsWith("edit")) {
                doEdit(out);
            } else {
                doView(out);
            }

        } catch (java.io.IOException ex) {
            throw new JspException("Error in EditButton tag", ex);
        }
    }

    private void doView(JspWriter out) throws IOException {
        out.print("<span class=\"editbutton\">"
                + "<a href=\"#\""
                + " onClick=\"editthis(" + form + ", '" + target + "');\">edit</a>"
                + "</span>");
    }

    private void doEdit(JspWriter out) throws IOException {
        if (mode.endsWith(target)) {
            out.print("<span class=\"editbutton\">"
                    + "<a href=\"#\""
                    + " onClick=\"savethis(" + form + ", '" + target + "');\">save</a>&nbsp;&nbsp;"
                    + "<a href=\"#\""
                    + " onClick=\"setMode(" + form + ", 'view');" + form + ".submit(); \">cancel</a>"
                    + "</span>");
        }
    }

    private void doAdd(JspWriter out) throws IOException {
        out.println();
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setForm(String form) {
        this.form = form;
    }
}
