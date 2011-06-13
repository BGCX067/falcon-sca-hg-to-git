/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.tags;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import org.sca.calontir.cmpe.data.AuthType;
import org.sca.calontir.cmpe.data.Authorization;

/**
 *
 * @author rik
 */
public class AuthorizationsTag extends CMPExtendedTagSupport {

    private List<Authorization> authorizations;
    private List<AuthType> authTypes;
    // internal for lookup;
    private List<Long> authorizationIds;
    private Map<Long, AuthType> authTypeMap;

    @Override
    protected void doAdd(JspWriter out) throws IOException {
        for (AuthType at : this.authTypes) {
            out.print("<input type=\"checkbox\" name=\"authorization\" value=\"");
            out.print(at.getAuthTypeId().getId() + "\" ");
            if (authorizationIds.contains(at.getAuthTypeId().getId())) {
                out.print(" checked ");
            }
            out.print(" />");
            out.print(at.getCode());
        }
    }

    @Override
    protected void doEdit(JspWriter out) throws IOException {
        for (AuthType at : this.authTypes) {
            out.print("<input type=\"checkbox\" name=\"authorization\" value=\"");
            out.print(at.getAuthTypeId().getId() + "\" ");
            if (authorizationIds.contains(at.getAuthTypeId().getId())) {
                out.print(" checked ");
            }
            out.print(" />");
            out.print(at.getCode());
        }
    }

    @Override
    protected void doView(JspWriter out) throws IOException {
        boolean first = true;
        if (authorizations != null) {
            for (Authorization a : authorizations) {
                if (first) {
                    first = false;
                } else {
                    out.print(" ; ");
                }
                long authTypeId = a.getAuthType().getId();
                AuthType at = authTypeMap.get(authTypeId);
                out.print(at.getCode());
            }
        }
    }

    public void setAuthorizations(List<Authorization> authorizations) {
        this.authorizations = authorizations;
        authorizationIds = new LinkedList<Long>();
        if (this.authorizations != null) {
            for (Authorization a : this.authorizations) {
                authorizationIds.add(a.getAuthorizatoinId().getId());
            }
        }
    }

    public void setAuthTypes(List<AuthType> authTypes) {
        this.authTypes = authTypes;
        authTypeMap = new LinkedHashMap<Long, AuthType>();
        if (this.authTypes != null) {
            for (AuthType at : this.authTypes) {
                authTypeMap.put(at.getAuthTypeId().getId(), at);
            }
        }
    }
}
