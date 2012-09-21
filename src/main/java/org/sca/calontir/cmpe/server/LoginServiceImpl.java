package org.sca.calontir.cmpe.server;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.sca.calontir.cmpe.client.LoginInfo;
import org.sca.calontir.cmpe.client.LoginService;
import org.sca.calontir.cmpe.db.FighterDAO;
import org.sca.calontir.cmpe.dto.Fighter;

public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {
	private static final Logger log = Logger.getLogger(LoginServiceImpl.class.getName());
	private static final Map<String, String> openIdProviders;
    static {
        openIdProviders = new HashMap<String, String>();
        openIdProviders.put("Google", "https://www.google.com/accounts/o8/id");
        openIdProviders.put("Yahoo", "yahoo.com");
        openIdProviders.put("MySpace", "myspace.com");
        openIdProviders.put("AOL", "aol.com");
        openIdProviders.put("MyOpenId.com", "myopenid.com");
    }

	@Override
	public LoginInfo login(String loginTargetUri, String logoutTargetUri, String provider) {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        LoginInfo loginInfo = new LoginInfo();

        if (user != null) {
            loginInfo.setLoggedIn(true);
            loginInfo.setEmailAddress(user.getEmail());
            loginInfo.setNickname(user.getNickname());
            loginInfo.setLogoutUrl(userService.createLogoutURL(logoutTargetUri));
            FighterDAO fDao = new FighterDAO();
            Fighter f_ub = fDao.getFighterByGoogleId(user.getEmail()); // TODO: Change to only returning SCA Name, not entire object.
            if (f_ub != null) {
                loginInfo.setScaName(f_ub.getScaName());
                loginInfo.setUserRole(f_ub.getRole());
                loginInfo.setFighterId(f_ub.getFighterId());
            } else {
				log.info(String.format("Cannot find a user for %s; Email: %s Federated Id: %s", user.getNickname(), user.getEmail(), user.getFederatedIdentity()));
			}
        } else {
            loginInfo.setLoggedIn(false);
			Set<String> attributes = new HashSet<String>();
            loginInfo.setLoginUrl(userService.createLoginURL(loginTargetUri, null, openIdProviders.get(provider), attributes));
        }
        return loginInfo;
    }
}
