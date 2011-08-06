package org.sca.calontir.cmpe.user;

import com.google.appengine.api.users.User;
import org.sca.calontir.cmpe.common.UserRoles;
import org.sca.calontir.cmpe.dto.Fighter;

/**
 *
 * @author rik
 */
public class Security {

    private User aeUser;
    private Fighter user;

    protected Security() {
    }

    public boolean isRole(UserRoles userRole) {
        return user == null ? false : user.getRole().equals(userRole);
    }
    
    public boolean isRoleOrGreater(UserRoles userRole) {
        if(user != null)
        System.out.println("Comparing user " + user.getScaName() + " with role " + user.getRole());
        else
            System.out.println("User null");
        return user == null ? false : user.getRole().compareTo(userRole) >= 0;
    }

    protected User getAeUser() {
        return aeUser;
    }

    protected void setAeUser(User aeUser) {
        this.aeUser = aeUser;
    }

    protected Fighter getUser() {
        return user;
    }

    protected void setUser(Fighter user) {
        this.user = user;
    }
    
    
}
