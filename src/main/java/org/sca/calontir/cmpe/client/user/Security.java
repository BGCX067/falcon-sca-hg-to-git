/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.user;

import org.sca.calontir.cmpe.client.FighterInfo;
import org.sca.calontir.cmpe.client.LoginInfo;
import org.sca.calontir.cmpe.client.ui.LookupController;
import org.sca.calontir.cmpe.common.UserRoles;
import org.sca.calontir.cmpe.dto.Fighter;
import org.sca.calontir.cmpe.dto.ScaGroup;

/**
 *
 * @author rikscarborough
 */
public class Security {

    private LoginInfo loginInfo;

    protected Security() {
    }

    public boolean isLoggedIn() {
        if (loginInfo == null) {
            return false;
        }
        return loginInfo.isLoggedIn();
    }

    public LoginInfo getLoginInfo() {
        return loginInfo;
    }

    public void setLoginInfo(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    public boolean isRole(UserRoles userRole) {
        if (loginInfo == null || loginInfo.getUserRole() == null) {
            return false;
        }
        return loginInfo.getUserRole().equals(userRole);
    }

    public boolean isRoleOrGreater(UserRoles userRole) {
        if (loginInfo == null || loginInfo.getUserRole() == null) {
            return false;
        }
        return loginInfo.getUserRole().ordinal() >= userRole.ordinal();
    }

    public boolean canEditAuthorizations(Long fighterId) {
        if (loginInfo == null) {
            return false;
        }

        if (isRoleOrGreater(UserRoles.CARD_MARSHAL)) {
            return true;
        }

        return false;
    }

    public boolean canEditFighter(Fighter fighter) {
        return canEditFighter(fighter.getFighterId(), fighter.getScaGroup());
    }

    private synchronized boolean canEditFighter(Long fighterId, ScaGroup userGroup) {
        if (loginInfo == null || !loginInfo.isLoggedIn()) {
            return false;
        }

        if (isRoleOrGreater(UserRoles.CARD_MARSHAL)) {
            return true;
        }

        if (loginInfo.getFighterId() > 0 && loginInfo.getFighterId() == fighterId) {
            return true;
        }

        if (isRole(UserRoles.KNIGHTS_MARSHAL)) {
            ScaGroup fightersGroup = LookupController.getInstance().getScaGroup(loginInfo.getGroup());
            if (userGroup != null && fightersGroup != null) {
                if (userGroup.getGroupName().equals(fightersGroup.getGroupName())) {
                    return true;
                }
            }
        }

        if (isRole(UserRoles.DEPUTY_EARL_MARSHAL)) {
            ScaGroup fightersGroup = LookupController.getInstance().getScaGroup(loginInfo.getGroup());
            if (userGroup != null && fightersGroup != null) {
                if (userGroup.getGroupLocation().equals(fightersGroup.getGroupLocation())) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean canView(FighterInfo fighter) {
        return canEditFighter(fighter.getFighterId(), LookupController.getInstance().getScaGroup(fighter.getGroup()));
    }
}
