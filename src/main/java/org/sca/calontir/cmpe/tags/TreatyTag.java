/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.sca.calontir.cmpe.tags;

import java.io.IOException;
import javax.servlet.jsp.JspWriter;
import org.sca.calontir.cmpe.dto.Treaty;

/**
 *
 * @author rik
 */
public class TreatyTag extends CMPExtendedTagSupport {

    private Treaty treaty;

    @Override
    protected void doView(JspWriter out) throws IOException {
        if (treaty != null) {
            out.println("Treaty");
        }
    }

    @Override
    protected void doEdit(JspWriter out) throws IOException {
        out.print("<input type=\"checkbox\" name=\"treaty\" value=\"treaty\"");
        if (treaty != null) {
            out.print(" checked ");
        }
        out.print(" />");
        out.println("Treaty");
    }

    @Override
    protected void doAdd(JspWriter out) throws IOException {
        out.print("<input type=\"checkbox\" name=\"treaty\" value=\"treaty\"");
        if (treaty != null) {
            out.print(" checked ");
        }
        out.print(" />");
        out.println("Treaty");
    }

    public void setTreaty(Treaty treaty) {
        this.treaty = treaty;
    }
}
