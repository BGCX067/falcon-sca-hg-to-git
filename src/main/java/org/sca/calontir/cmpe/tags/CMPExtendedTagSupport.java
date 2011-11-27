package org.sca.calontir.cmpe.tags;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author rik
 */
public abstract class CMPExtendedTagSupport extends SimpleTagSupport {

    protected String mode;
    protected String editMode = "";

    @Override
    public void doTag() throws JspException {
        JspWriter out = getJspContext().getOut();

        try {
            init();

            if (mode != null && mode.equals("add")) {
                doAdd(out);
            } else if ((StringUtils.isBlank(editMode) && mode.startsWith("edit"))
                    || (mode.equals(editMode))) {
                doEdit(out);
            } else {
                doView(out);
            }
        } catch (java.io.IOException ex) {
            throw new JspException(String.format("Error in %s tag", getClass().getName()), ex);
        }
    }

    protected void init() {
    }

    abstract protected void doView(JspWriter out) throws IOException;

    abstract protected void doEdit(JspWriter out) throws IOException;

    abstract protected void doAdd(JspWriter out) throws IOException;

    public void setEditMode(String editMode) {
        this.editMode = editMode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
