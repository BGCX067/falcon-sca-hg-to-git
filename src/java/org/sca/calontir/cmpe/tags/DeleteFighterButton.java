package org.sca.calontir.cmpe.tags;

import java.io.IOException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 *
 * @author rik
 */
public class DeleteFighterButton extends SimpleTagSupport {

    private String mode;

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
        out.print("<span class=\"deleteButton\">"
                + "<a href=\"#\" id=\"Delete\" class=\"Delete\""
                + " onClick=\"$( '#dialog-confirm' ).dialog('open')\">Delete</a>"
                + "</span>");
    }

    private void doEdit(JspWriter out) throws IOException {
        out.println();
    }

    private void doAdd(JspWriter out) throws IOException {
        out.println();
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

}
