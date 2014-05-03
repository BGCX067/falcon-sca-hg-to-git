package org.sca.calontir.cmpe.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.sca.calontir.cmpe.client.ui.CalonBar;
import org.sca.calontir.cmpe.client.ui.DataUpdatedEvent;
import org.sca.calontir.cmpe.client.ui.EditViewEvent;
import org.sca.calontir.cmpe.client.ui.FighterFormWidget;
import org.sca.calontir.cmpe.client.ui.FighterListBox;
import org.sca.calontir.cmpe.client.ui.LookupController;
import org.sca.calontir.cmpe.client.ui.Mode;
import org.sca.calontir.cmpe.client.ui.SearchBar;
import org.sca.calontir.cmpe.client.ui.SearchEvent;
import org.sca.calontir.cmpe.client.ui.Shout;
import org.sca.calontir.cmpe.client.user.Security;
import org.sca.calontir.cmpe.client.user.SecurityFactory;
import org.sca.calontir.cmpe.dto.Fighter;

/**
 * Entry Point.
 *
 * Design: 1. Starts application with information screen "Loading..." 2. Check status of user, i.e. is logged in 3. Async a.
 * Begin loading data, both from localstorage and server. b. Build screens. Index page in front. Search button disabled. 4.
 * When data is loaded, enable search button.
 *
 * @author Rik Scarborough
 */
public class IndexPage implements EntryPoint {

    private static final Logger log = Logger.getLogger(IndexPage.class.getName());
    final private Security security = SecurityFactory.getSecurity();
    private FighterFormWidget fighterFormWidget;
    private FighterListBox flb;
    private Panel tilePanel;
    private final Shout shout = Shout.getInstance();

    /**
     * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
     */
    @Override
    public void onModuleLoad() {
        GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
            @Override
            public void onUncaughtException(Throwable e) {
                DisplayUtils.clearDisplay();
                Shout.getInstance().tell("Ehue -- We've encountered an error. The error has been logged,  and we will take a look. In the meantime please refresh by clicking on \"Home\" or hit F5 on your keyboard.", false);
                log.log(Level.SEVERE, "Uncaught Exception", e);
            }
        });
        String userAgent = Window.Navigator.getUserAgent().toLowerCase();
        if (userAgent.contains("msie 7") || userAgent.contains("msie 6")) {
            shout.tell("This application will not work with any verison of IE below 8.\n"
                    + "Please upgrade to a more modern browser such as Chrome, FireFox, or IE8.\n"
                    + "The application works best in Chrome.");
            return;
        }
        shout.tell("Please wait, retrieving data");
        LookupController.getInstance();
        final Timer t = new Timer() {
            @Override
            public void run() {
                if (LookupController.getInstance().isDLComplete()) {
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            shout.tell("Updating Initial data");
                            LookupController.getInstance().updateLocalData();
                            onModuleLoad2();
                        }
                    });
                } else {
                    this.schedule(500);
                }
            }
        };

        t.schedule(500);

    }

    public void onModuleLoad2() {
        tilePanel = RootPanel.get("tile");
        //LookupController.getInstance();

        Image titleImage = new Image();
        titleImage.setUrl("images/title_image.gif");
        titleImage.addStyleName("titleImage");
        tilePanel.add(titleImage);

        Label titleLabel = new Label("FALCON");
        titleLabel.setWordWrap(false);
        titleLabel.setTitle("Fighter Authorization List Calontir ONline (FALCON)");
        titleLabel.setStyleName("title");

        tilePanel.add(titleLabel);
        LoginServiceAsync loginService = GWT.create(LoginService.class);
        loginService.login(GWT.getHostPageBaseURL(), GWT.getHostPageBaseURL(),
                new AsyncCallback<LoginInfo>() {
                    @Override
                    public void onFailure(Throwable error) {
                        Window.alert("Error in contacting the server, try later");
                    }

                    @Override
                    public void onSuccess(LoginInfo loginInfo) {
                        boolean needToRegister = false;
                        SecurityFactory.setLoginInfo(loginInfo);
                        if (security.isLoggedIn()) {
                            final Label hello;
                            if (loginInfo.isUser()) {
                                needToRegister = false;
                                hello = new Label("Welcome " + loginInfo.getScaName());
                                log.info("Logged in as " + loginInfo.getScaName() + ":" + loginInfo.getNickname());
                            } else {
                                needToRegister = true;
                                hello = new Label("Welcome " + loginInfo.getNickname());
                                log.info("Logged in with " + loginInfo.getNickname());
                            }
                            hello.setStyleName("hello", true);
                            tilePanel.add(hello);
                        }

                        shout.tell("Building pages.");
                        buildIndexPage();
                        shout.hide();
                        if (needToRegister) {
                            // this user was not found in our system.
                            shout.tell("A Falcon user for "
                                    + loginInfo.getNickname()
                                    + " was not found. Please register if you have not, or contact the support team if there is a problem.", false);
                        }
                    }
                });

        History.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                String historyToken = event.getValue();
                try {
                    if (historyToken.substring(0, 8).equals("display:")) {
                        String display = historyToken.substring(8);
                        // Select the specified tab panel
                        DisplayUtils.changeDisplay(DisplayUtils.Displays.valueOf(display));
                    } else if (historyToken.startsWith("qrtlyreport:")) {
                        DisplayUtils.clearDisplay();
                        DisplayUtils.changeDisplay(DisplayUtils.Displays.ReportGen);
                    } else {
                        DisplayUtils.resetDisplay();
                        DisplayUtils.changeDisplay(DisplayUtils.Displays.SignupForm);
                    }

                } catch (IndexOutOfBoundsException e) {
                    DisplayUtils.changeDisplay(DisplayUtils.Displays.SignupForm);
                }
            }
        });

    }

    private void buildIndexPage() {
        // remove Loading-Message from page
        tilePanel.getElement().removeChild(DOM.getElementById("Loading-Message"));

        CalonBar calonBar = new CalonBar();
        tilePanel.add(calonBar);

        flb = new FighterListBox();
        fighterFormWidget = new FighterFormWidget();
        SearchBar searchBar = new SearchBar();
        searchBar.getElement().setId("SearchBar");
        searchBar.addHandler(fighterFormWidget, EditViewEvent.TYPE);
        searchBar.addHandler(flb, SearchEvent.TYPE);
        searchBar.addHandler(searchBar, SearchEvent.TYPE);
        fighterFormWidget.addHandler(searchBar, DataUpdatedEvent.TYPE);
        tilePanel.add(searchBar);

        final LoginInfo loginInfo = security.getLoginInfo();
        if (!loginInfo.isUser()) {
            buildSignUp(loginInfo);
        }
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                log.log(Level.SEVERE, reason.getMessage(), reason);
            }

            @Override
            public void onSuccess() {
                foundMultibleResults();
                buildFighterForm();
                if (loginInfo.isUser()) {
                    FighterServiceAsync fighterService = GWT.create(FighterService.class);

                    fighterService.getFighter(loginInfo.getFighterId(), new AsyncCallback<Fighter>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            log.severe("getFighter " + caught.getMessage());
                        }

                        @Override
                        public void onSuccess(Fighter result) {
                            flb.fireEvent(new EditViewEvent(Mode.VIEW, result));
                        }
                    });
                }
            }
        });
    }

    private void buildSignUp(LoginInfo loginInfo) {
        final Panel signupPanel = new FlowPanel();
        signupPanel.setStyleName("dataBox");
        signupPanel.getElement().setId(DisplayUtils.Displays.SignupForm.toString());

        final Panel innerSignupPanel = new FlowPanel();
        innerSignupPanel.setStyleName("dataBody");
        innerSignupPanel.setStyleName("signupPanel", true);
        innerSignupPanel.getElement().setId("innerSignupPanel");

        if (!loginInfo.isLoggedIn()) {
            final Anchor signInLink = new Anchor("Sign In with Google ID");
            signInLink.setHref(loginInfo.getLoginUrl());
            signInLink.getElement().getStyle().setFloat(Style.Float.LEFT);
            innerSignupPanel.add(signInLink);
        }

        final Anchor register = new Anchor("Not registered? Sign up now!");
        register.setHref("https://docs.google.com/spreadsheet/viewform?formkey=dEhpX0tCWmhGRU9tNjF4OVdtTjZpcHc6MQ");
        register.getElement().getStyle().setFloat(Style.Float.RIGHT);
        innerSignupPanel.add(register);

        signupPanel.add(innerSignupPanel);

        tilePanel.add(signupPanel);
    }

    private void foundMultibleResults() {
        flb.addHandler(fighterFormWidget, EditViewEvent.TYPE);
        tilePanel.add(flb);
    }

    private void buildFighterForm() {
        final FormPanel fighterForm = new FormPanel();
        fighterForm.setAction("/FighterServlet");
        fighterForm.getElement().setId(DisplayUtils.Displays.FighterForm.toString());
        fighterForm.getElement().getStyle().setDisplay(Style.Display.NONE);
        fighterForm.setMethod(FormPanel.METHOD_POST);

        fighterFormWidget.setForm(fighterForm);
        fighterForm.add(fighterFormWidget);
        fighterForm.addSubmitHandler(fighterFormWidget);
        fighterForm.addSubmitCompleteHandler(fighterFormWidget);

        tilePanel.add(fighterForm);
    }
}
