package org.sca.calontir.cmpe.user;

import com.google.appengine.api.users.User;
import org.sca.calontir.cmpe.common.UserRoles;
import org.sca.calontir.cmpe.db.FighterDAO;
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

    public boolean canPrintFighter(Long fighterId) {
        if (user == null) {
            return false;
        }
        
        if (isRoleOrGreater(UserRoles.CARD_MARSHAL)) {
            return true;
        }

        if (user.getFighterId() != null && user.getFighterId().longValue() == fighterId.longValue()) {
            return true;
        }

        FighterDAO fighterDao = new FighterDAO();
        Fighter fighter = fighterDao.getFighter(fighterId);

        if (isRole(UserRoles.KNIGHTS_MARSHAL)) {
            if (user.getScaGroup() != null && fighter.getScaGroup() != null) {
                if (user.getScaGroup().getGroupName().equals(fighter.getScaGroup().getGroupName())) {
                    return true;
                }
            }
        }
        
        if (isRole(UserRoles.DEPUTY_EARL_MARSHAL)) {
            if (user.getScaGroup() != null && fighter.getScaGroup() != null) {
                if (user.getScaGroup().getGroupLocation().equals(fighter.getScaGroup().getGroupLocation())) {
                    return true;
                }
            }
        }

        return false;
    }
}
