/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import org.sca.calontir.cmpe.client.LoginInfo;
import org.sca.calontir.cmpe.client.LoginService;
import org.sca.calontir.cmpe.client.LoginServiceAsync;

/**
 *
 * @author rikscarborough
 */
public class CalonBar extends Composite {

    private Panel barPanel = new FlowPanel();
    private LoginInfo loginInfo = null;
    private Anchor homeLink = new Anchor("Home");
    private Anchor signInLink = new Anchor("Sign In");
    private Anchor signOutLink = new Anchor("Sign Out");
    private Label loginLabel = new Label("Please sign in to your Google Account to access the StockWatcher application.");

    public CalonBar() {

        barPanel.setStylePrimaryName("calonbar");

        homeLink.setHref("/index.html");
        homeLink.setStylePrimaryName("calonbarlink");
        barPanel.add(homeLink);

        Label k = new Label();
        k.setText("|");
        barPanel.add(k);

        LoginServiceAsync loginService = GWT.create(LoginService.class);
        loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {

            public void onFailure(Throwable error) {
            }

            public void onSuccess(LoginInfo result) {
                loginInfo = result;
                if (loginInfo.isLoggedIn()) {
                    signOutLink.setHref(loginInfo.getLogoutUrl());
                    signOutLink.setStylePrimaryName("calonbarlink");
                    if (loginInfo.getScaName() == null) {
                        signOutLink.setText(loginInfo.getNickname());
                    } else {
                        signOutLink.setText(loginInfo.getScaName());
                    }
                    barPanel.add(signOutLink);
                } else {
                    loadLogin();
                }
            }
        });

        initWidget(barPanel);
    }

    private void loadLogin() {
        // Assemble login panel.
        signInLink.setHref(loginInfo.getLoginUrl());
        signInLink.setStylePrimaryName("calonbarlink");
//        loginPanel.add(loginLabel);
        barPanel.add(signInLink);
    }
}
