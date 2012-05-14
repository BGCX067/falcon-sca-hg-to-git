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
		if (loginInfo == null || loginInfo.getUserRole() == null) {
			return false;
		}
		return loginInfo.getUserRole().equals(userRole);
	}

	public boolean isRoleOrGreater(UserRoles userRole) {
		if (loginInfo == null || loginInfo.getUserRole() == null) {
			return false;
		}
		return loginInfo.getUserRole().compareTo(userRole) >= 0;
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

	public boolean canEditFighter(Long fighterId) {
		if (loginInfo == null) {
			return false;
		}

		if (isRoleOrGreater(UserRoles.CARD_MARSHAL)) {
			return true;
		}

		if (loginInfo.getFighterId() > 0 && loginInfo.getFighterId() == fighterId.longValue()) {
			return true;
		}

//        FighterDAO fighterDao = new FighterDAO();
//        Fighter fighter = fighterDao.getFighter(fighterId);
//		Fighter user = fighterDao.getFighter(loginInfo.getFighterId());
//
//        if (isRole(UserRoles.KNIGHTS_MARSHAL)) {
//            if (user.getScaGroup() != null && fighter.getScaGroup() != null) {
//                if (user.getScaGroup().getGroupName().equals(fighter.getScaGroup().getGroupName())) {
//                    return true;
//                }
//            }
//        }
//
//        if (isRole(UserRoles.DEPUTY_EARL_MARSHAL)) {
//            if (user.getScaGroup() != null && fighter.getScaGroup() != null) {
//                if (user.getScaGroup().getGroupLocation().equals(fighter.getScaGroup().getGroupLocation())) {
//                    return true;
//                }
//            }
//        }

		return false;
	}

	public boolean canView(Long fighterId) {
		return canEditFighter(fighterId);
	}
}
