/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
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

    protected static final String CALONBAR = "calonbar";
    protected static final String INDEXHTML = "/index.html";
    protected static final String ABOUT_PAGE = "/about.jsp";
    protected static final String CALONBARLINK = "calonbarlink";
    protected static final String SIGN_IN_TEXT = "Please sign in to your Google Account.";
    protected static final String SIGN_OUT_TEXT = "This will log you out of your Google Account.";
    private Panel barPanel = new FlowPanel();
    private Panel loginPanel = new FlowPanel();
    private LoginInfo loginInfo = null;
    private Anchor homeLink = new Anchor("Home");
    private Anchor aboutLink = new Anchor("About");
    private Anchor signInLink = new Anchor("Sign In");
    private Anchor signOutLink = new Anchor("Sign Out");

    public CalonBar() {

        DOM.setElementAttribute(barPanel.getElement(), "id", CALONBAR);

        homeLink.setHref(INDEXHTML);
        homeLink.setStyleName(CALONBARLINK);
        barPanel.add(homeLink);

        barPanel.add(getDivBar());
        
        loginPanel.setStyleName(CALONBARLINK);
        barPanel.add(loginPanel);

        LoginServiceAsync loginService = GWT.create(LoginService.class);
        loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {

            public void onFailure(Throwable error) {
            }

            public void onSuccess(LoginInfo result) {
                loginInfo = result;
                if (loginInfo.isLoggedIn()) {
                    loadLogout();
                } else {
                    loadLogin();
                }
            }
        });

        barPanel.add(getDivBar());

        aboutLink.setHref(ABOUT_PAGE);
        aboutLink.setStyleName(CALONBARLINK);
        barPanel.add(aboutLink);

        initWidget(barPanel);
    }

    private Label getDivBar() {
        Label divBar = new Label();
        divBar.setText("|");
        divBar.setStyleName(CALONBARLINK);
        return divBar;
    }

    private void loadLogin() {
        signInLink.setHref(loginInfo.getLoginUrl());
        signInLink.setStyleName(CALONBARLINK);
        signInLink.setTitle(SIGN_IN_TEXT);
        loginPanel.add(signInLink);
    }

    private void loadLogout() {
        signOutLink.setHref(loginInfo.getLogoutUrl());
        signOutLink.setStyleName(CALONBARLINK);
        signOutLink.setTitle(SIGN_OUT_TEXT);
        if (loginInfo.getScaName() == null) {
            signOutLink.setText(loginInfo.getNickname());
        } else {
            signOutLink.setText(loginInfo.getScaName());
        }
        loginPanel.add(signOutLink);
    }
}
