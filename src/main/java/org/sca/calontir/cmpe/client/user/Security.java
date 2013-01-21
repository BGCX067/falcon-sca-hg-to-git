/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.user;

import com.google.gwt.user.client.Window;
import org.sca.calontir.cmpe.client.FighterInfo;
import org.sca.calontir.cmpe.client.LoginInfo;
import org.sca.calontir.cmpe.client.ui.LookupController;
import org.sca.calontir.cmpe.common.UserRoles;
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

	public synchronized boolean canEditFighter(Long fighterId) {
		if (loginInfo == null) {
			return false;
		}

		if (isRoleOrGreater(UserRoles.CARD_MARSHAL)) {
			return true;
		}

		if (loginInfo.getFighterId() > 0 && loginInfo.getFighterId() == fighterId.longValue()) {
			return true;
		}

		FighterInfo fi = LookupController.getInstance().getFighter(fighterId);
		FighterInfo user = LookupController.getInstance().getFighter(loginInfo.getFighterId());
		if(user == null) {
			return false;
		}

		if (isRole(UserRoles.KNIGHTS_MARSHAL) || isRole(UserRoles.GROUP_MARSHAL)) {
			ScaGroup userGroup = LookupController.getInstance().getScaGroup(user.getGroup());
			ScaGroup fightersGroup = LookupController.getInstance().getScaGroup(fi.getGroup());
			if (userGroup != null && fightersGroup != null) {
				if (userGroup.getGroupName().equals(fightersGroup.getGroupName())) {
					return true;
				}
			}
		}

		if (isRole(UserRoles.DEPUTY_EARL_MARSHAL)) {
			ScaGroup userGroup = LookupController.getInstance().getScaGroup(user.getGroup());
			ScaGroup fightersGroup = LookupController.getInstance().getScaGroup(fi.getGroup());
			if (userGroup != null && fightersGroup != null) {
				if (userGroup.getGroupLocation().equals(fightersGroup.getGroupLocation())) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean canView(Long fighterId) {
		return canEditFighter(fighterId);
	}
}
