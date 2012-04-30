/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.user;

import org.sca.calontir.cmpe.client.LoginInfo;
import org.sca.calontir.cmpe.common.UserRoles;

/**
 *
 * @author rikscarborough
 */
public class Security {

    private LoginInfo loginInfo;

    protected Security() {
    }

    public LoginInfo getLoginInfo() {
        return loginInfo;
    }

    public void setLoginInfo(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    public boolean isRole(UserRoles userRole) {
		if(loginInfo == null || loginInfo.getUserRole() == null)
			return false;
        return loginInfo.getUserRole().equals(userRole);
    }

    public boolean isRoleOrGreater(UserRoles userRole) {
		if(loginInfo == null || loginInfo.getUserRole() == null)
			return false;
        return loginInfo.getUserRole().compareTo(userRole) >= 0;
    }

    public boolean canEdit(Long fighterId) {
        if (loginInfo == null) {
            return false;
        }
        if (isRoleOrGreater(UserRoles.CARD_MARSHAL)) {
            return true;
        }
		return false;
    }

    public boolean canView(Long fighterId) {
        if (loginInfo == null) {
            return false;
        }

        if (isRoleOrGreater(UserRoles.CARD_MARSHAL)) {
            return true;
        }

        if (loginInfo.getFighterId() == fighterId) {
            return true;
        }
        
        return false;
    }
}
