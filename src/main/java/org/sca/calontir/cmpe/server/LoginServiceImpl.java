package org.sca.calontir.cmpe.server;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.util.logging.Logger;
import org.sca.calontir.cmpe.client.LoginInfo;
import org.sca.calontir.cmpe.client.LoginService;
import org.sca.calontir.cmpe.db.FighterDAO;
import org.sca.calontir.cmpe.dto.Fighter;

public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {

    private static final Logger log = Logger.getLogger(LoginServiceImpl.class.getName());

    @Override
    public LoginInfo login(String loginTargetUri, String logoutTargetUri) {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        LoginInfo loginInfo = new LoginInfo();

        if (user != null) {
            loginInfo.setLoggedIn(true);
            loginInfo.setEmailAddress(user.getEmail());
            loginInfo.setNickname(user.getNickname());
            loginInfo.setLogoutUrl(userService.createLogoutURL(logoutTargetUri));
            FighterDAO fDao = new FighterDAO();
            Fighter f_ub = fDao.getFighterByGoogleId(user.getEmail());
            if (f_ub != null) {
                loginInfo.setScaName(f_ub.getScaName());
                loginInfo.setUserRole(f_ub.getRole());
                loginInfo.setFighterId(f_ub.getFighterId());
                loginInfo.setGroup(f_ub.getScaGroup().getGroupName());
            } else {
                log.info(String.format("Cannot find a user for %s; Email: %s Federated Id: %s", user.getNickname(), user.getEmail(), user.getFederatedIdentity()));
            }
        } else {
            loginInfo.setLoggedIn(false);
            loginInfo.setLoginUrl(userService.createLoginURL(loginTargetUri));
        }
        return loginInfo;
    }
}
