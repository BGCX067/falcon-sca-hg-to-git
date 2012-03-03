package org.sca.calontir.cmpe.server;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.sca.calontir.cmpe.client.LoginInfo;
import org.sca.calontir.cmpe.client.LoginService;
import org.sca.calontir.cmpe.db.FighterDAO;
import org.sca.calontir.cmpe.dto.Fighter;

public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {

    public LoginInfo login(String requestUri) {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        LoginInfo loginInfo = new LoginInfo();

        if (user != null) {
            loginInfo.setLoggedIn(true);
            loginInfo.setEmailAddress(user.getEmail());
            loginInfo.setNickname(user.getNickname());
            loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
            FighterDAO fDao = new FighterDAO();
            Fighter f_ub = fDao.getFighterByGoogleId(user.getEmail()); // TODO: Change to only returning SCA Name, not entire object.
            if (f_ub != null) {
                loginInfo.setScaName(f_ub.getScaName());
            }
        } else {
            loginInfo.setLoggedIn(false);
            loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
        }
        return loginInfo;
    }
}
