package org.sca.calontir.cmpe.client.user;

import org.sca.calontir.cmpe.client.LoginInfo;

/**
 *
 * @author rikscarborough
 */
public class SecurityFactory {
    private static Security _instance = new Security();
    
    private SecurityFactory() {
        
    }
    
    public static void setLoginInfo(LoginInfo loginInfo) {
        _instance.setLoginInfo(loginInfo);
    }
    
    public static Security getSecurity() { 
        return _instance;
    }
    
}
