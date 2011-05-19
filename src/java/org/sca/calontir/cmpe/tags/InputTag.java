/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.tags;

import com.google.appengine.repackaged.com.google.common.base.StringUtil;
import com.google.appengine.repackaged.org.apache.commons.codec.binary.StringUtils;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 *
 * @author rik
 */
public class InputTag extends SimpleTagSupport {

    private String mode;
    private String name;
    private String value;
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

        try {
            JspFragment f = getJspBody();
            if (f != null) {
                f.invoke(out);
            }

            if (mode != null && mode.equals("add")  && !type.equals("viewonly")) {
                out.print("<input type=" + type + " name=" + name);
                if(value != null && !StringUtil.isEmptyOrWhitespace(value))
                    out.print(" value=" + value);
                if(size > 0)
                    out.print(" size=" + size);
                if (type.equalsIgnoreCase("text")) {
                    out.print(" onfocus=\"if (this.value==this.defaultValue) this.value='';"
                            + " else this.select()\" onblur=\"if (!this.value) this.value=this.defaultValue)\"");
                }
                out.println(" />");
            } else {
                if(value != null && !("submit".equals(type)))
                    out.println(value);
            }

        } catch (java.io.IOException ex) {
            throw new JspException("Error in InputTag tag", ex);
        }
    }

    public void setValue(String value) {
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
