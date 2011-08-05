/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.user;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import org.sca.calontir.cmpe.dto.Fighter;
import org.sca.calontir.cmpe.db.FighterDAO;

/**
 *
 * @author rik
 */
public class SecurityFactory {

    private SecurityFactory() {
    }

    public static Security getSecurity() {
        Security security = new Security();
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        security.setAeUser(user);
        
        FighterDAO fighterDao = new FighterDAO();
        Fighter fighter = fighterDao.getFighterByGoogleId(user.getUserId());
        security.setUser(fighter);

        return security;
    }
}
