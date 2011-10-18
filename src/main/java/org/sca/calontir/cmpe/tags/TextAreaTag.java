package org.sca.calontir.cmpe.tags;

import java.io.IOException;
import javax.servlet.jsp.JspWriter;

/**
 *
 * @author rik
 */
public class TextAreaTag extends CMPExtendedTagSupport {

    private String name;
    private String value;

    @Override
    protected void doView(JspWriter out) throws IOException {
        out.print("<textarea name=\"");
        out.print(name);
        out.print("\" readonly=\"true\">");
        if (value != null) {
            out.print(value);
        }
        out.print("</textarea>");
    }

    @Override
    protected void doEdit(JspWriter out) throws IOException {
        out.print("<textarea name=\"");
        out.print(name);
        out.print("\">");
        if (value != null) {
            out.print(value);
        }
        out.print("</textarea>");
    }

    @Override
    protected void doAdd(JspWriter out) throws IOException {
        out.print("<textarea name=\"");
        out.print(name);
        out.print("\">");
        out.print("</textarea>");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
