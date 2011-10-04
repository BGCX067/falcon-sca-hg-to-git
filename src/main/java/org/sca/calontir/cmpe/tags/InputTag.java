package org.sca.calontir.cmpe.tags;

import java.io.IOException;
import java.util.Date;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

/**
 *
 * @author rik
 */
public class InputTag extends CMPExtendedTagSupport {

    private String name;
    private String id;
    private Object value;
    private String type;
    private Integer size = 0;
    
    // local
    String valueOut = "";

    /**
     * Called by the container to invoke this tag. 
     * The implementation of this method is provided by the tag library developer,
     * and handles all tag processing, body iteration, etc.
     */
    @Override
    public void doTag() throws JspException {
        JspWriter out = getJspContext().getOut();
    
        if (value instanceof String) {
            valueOut = (String) value;
        } else if (value instanceof Date) {
            if (value != null) {
                DateTime dt = new DateTime(((Date) value).getTime());
                valueOut = dt.toString("MM/dd/yyyy");
            }
        } else if (value instanceof Boolean) {
            valueOut = ((Boolean) value) ? "true" : "false";
        } else {
            if (value != null) {
                valueOut = value.toString();
            }
        }
        try {
            JspFragment f = getJspBody();
            if (f != null) {
                f.invoke(out);
            }

            if (mode != null && mode.equals("add") && !type.equals("viewonly")) {
                doAdd(out);
            } else if (mode.equals(editMode) && !type.equals("viewonly")) {
                doEdit(out);
            } else {
                doView(out);
            }

        } catch (java.io.IOException ex) {
            throw new JspException("Error in InputTag tag", ex);
        }
    }

    protected void doView(JspWriter out) throws IOException {
        if (valueOut != null && !("submit".equals(type))) {
            out.println(valueOut);
        }
    }

    protected void doEdit(JspWriter out) throws IOException {
        out.print("<input type=" + type );
        if (StringUtils.isNotBlank(name)) {
            out.print(" name=\"" + name + "\"");
        }
        if (StringUtils.isNotBlank(id)) {
            out.print(" id=\"" + id + "\"");
        }
        if (StringUtils.isNotBlank(valueOut)) {
            out.print(" value=\"" + valueOut + "\"");
        }
        if (size > 0) {
            out.print(" size=" + size);
        }
        out.println(" />");
    }

    protected void doAdd(JspWriter out) throws IOException {
        out.print("<input type=" + type );
        if (StringUtils.isNotBlank(name)) {
            out.print(" name=\"" + name + "\"");
        }
        if (StringUtils.isNotBlank(valueOut)) {
            out.print(" value=\"" + valueOut + "\"");
        }
        if (size > 0) {
            out.print(" size=" + size);
        }
        if (type.equalsIgnoreCase("text")) {
            out.print(" onfocus=\"if (this.value==this.defaultValue) this.value='';"
                    + " else this.select()\" onblur=\"if (!this.value) this.value=this.defaultValue)\"");
        }
        out.println(" />");
    }

    public void setValue(Object value) {
        this.value = value;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public void setType(String type) {
        this.type = type;
    }
    
}
