/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.tags;

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
public class AuthorizationsTag extends SimpleTagSupport {
    private String mode;
    private List<Authorization> authorizations;
    private List<AuthType> authTypes;
    
    // internal for lookup;
    private List<Long> authorizationIds;
    private Map<Long, AuthType> authTypeMap;

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
                for(AuthType at : this.authTypes) {
                    out.print("<input type=\"checkbox\" name=\"authorization\" value=\"" );
                    out.print(at.getCode() + "\" ");
                    if(authorizationIds.contains(at.getAuthTypeId().getId()))
                        out.print(" checked ");
                    out.print(" />");
                }
            } else {
                boolean first = false;
                for(Authorization a : authorizations) {
                    if(first) {
                        first = false;
                    } else {
                        out.print(" ; ");
                    }
                    long authTypeId = a.getAuthType().getId();
                    AuthType at = authTypeMap.get(authTypeId);
                    out.print(at.getCode());
                }
            }

        } catch (java.io.IOException ex) {
            throw new JspException("Error in AuthorizationsTag tag", ex);
        }
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setAuthorizations(List<Authorization> authorizations) {
        this.authorizations = authorizations;
        authorizationIds = new LinkedList<Long>();
        for(Authorization a : this.authorizations) {
            authorizationIds.add(a.getAuthorizatoinId().getId());
        }
    }

    public void setAuthTypes(List<AuthType> authTypes) {
        this.authTypes = authTypes;
        authTypeMap = new LinkedHashMap<Long, AuthType>();
        for(AuthType at : this.authTypes) {
            authTypeMap.put(at.getAuthTypeId().getId(), at);
        }
    }
}
