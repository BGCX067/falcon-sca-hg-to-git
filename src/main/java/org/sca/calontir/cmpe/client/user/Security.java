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
        this.loginInfo = loginInfo;
    }

    public LoginInfo getLoginInfo() {
        return loginInfo;
    }

    public void setLoginInfo(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    public boolean isRole(UserRoles userRole) {
        return loginInfo == null ? false : loginInfo.getUserRole().equals(userRole);
    }

    public boolean isRoleOrGreater(UserRoles userRole) {
        return loginInfo == null ? false : loginInfo.getUserRole().compareTo(userRole) >= 0;
    }

    public boolean canEdit(Long fighterId) {
        return canView(fighterId);
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
