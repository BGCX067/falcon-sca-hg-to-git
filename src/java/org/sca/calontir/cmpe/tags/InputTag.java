/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.tags;

import com.google.appengine.repackaged.com.google.common.base.StringUtil;
import java.util.Date;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import org.joda.time.DateTime;

/**
 *
 * @author rik
 */
public class InputTag extends SimpleTagSupport {

    private String mode;
    private String name;
    private Object value;
    private String type;
    private Integer size = 0;

    /**
     * Called by the container to invoke this tag. 
     * The implementation of this method is provided by the tag library developer,
     * and handles all tag processing, body iteration, etc.
     */
    @Override
    public void doTag() throws JspException {
        JspWriter out = getJspContext().getOut();

        String valueOut = "";
        if (value instanceof String) {
            valueOut = (String) value;
        } else if (value instanceof Date) {
            if (value != null) {
                DateTime dt = new DateTime(((Date) value).getTime());
                valueOut = dt.toString("MM/dd/yyyy");
            }
        } else if (value instanceof Boolean) {
            valueOut = ((Boolean)value) ? "true" : "false";
        } else {
            if(value != null)
            valueOut = value.toString();
        }
        try {
            JspFragment f = getJspBody();
            if (f != null) {
                f.invoke(out);
            }

            if (mode != null && mode.equals("add") && !type.equals("viewonly")) {
                out.print("<input type=" + type + " name=" + name);
                if (valueOut != null && !StringUtil.isEmptyOrWhitespace(valueOut)) {
                    out.print(" value=" + valueOut);
                }
                if (size > 0) {
                    out.print(" size=" + size);
                }
                if (type.equalsIgnoreCase("text")) {
                    out.print(" onfocus=\"if (this.value==this.defaultValue) this.value='';"
                            + " else this.select()\" onblur=\"if (!this.value) this.value=this.defaultValue)\"");
                }
                out.println(" />");
            } else {
                if (valueOut != null && !("submit".equals(type))) {
                    out.println(valueOut);
                }
            }

        } catch (java.io.IOException ex) {
            throw new JspException("Error in InputTag tag", ex);
        }
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public void setType(String type) {
        this.type = type;
    }
}
