package org.sca.calontir.cmpe.tags;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.jsp.JspWriter;
import org.sca.calontir.cmpe.dto.AuthType;
import org.sca.calontir.cmpe.dto.Authorization;

/**
 *
 * @author rik
 */
public class AuthorizationsTag extends CMPExtendedTagSupport {

    private List<Authorization> authorizations;
    private List<AuthType> authTypes;
    // internal for lookup;
    private List<String> authorizationIds;
    private Map<String, AuthType> authTypeMap;

    @Override
    protected void doAdd(JspWriter out) throws IOException {
        for (AuthType at : this.authTypes) {
            out.print("<input type=\"checkbox\" name=\"authorization\" value=\"");
            out.print(at.getCode() + "\" ");
            out.print(" />");
            out.print(at.getCode());
        }
    }

    @Override
    protected void doEdit(JspWriter out) throws IOException {
        for (AuthType at : this.authTypes) {
            out.print("<input type=\"checkbox\" name=\"authorization\" value=\"");
            out.print(at.getCode() + "\" ");
            if (authorizationIds.contains(at.getCode())) {
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
                out.print(a.getCode());
            }
        }
    }

    public void setAuthorizations(List<Authorization> authorizations) {
        this.authorizations = authorizations;
        authorizationIds = new LinkedList<String>();
        if (this.authorizations != null) {
            for (Authorization a : this.authorizations) {
                authorizationIds.add(a.getCode());
            }
        }
    }

    public void setAuthTypes(List<AuthType> authTypes) {
        this.authTypes = authTypes;
        authTypeMap = new LinkedHashMap<String, AuthType>();
        if (this.authTypes != null) {
            for (AuthType at : this.authTypes) {
                authTypeMap.put(at.getCode(), at);
            }
        }
    }
}
