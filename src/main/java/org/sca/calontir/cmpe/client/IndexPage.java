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
import com.google.gwt.user.client.ui.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.sca.calontir.cmpe.client.ui.*;
import org.sca.calontir.cmpe.client.user.Security;
import org.sca.calontir.cmpe.client.user.SecurityFactory;

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
	protected static final String GETTING_STARTED = "https://sites.google.com/site/calontirmmproject/support";
	final private Security security = SecurityFactory.getSecurity();
	private FighterFormWidget fighterFormWidget = new FighterFormWidget();
	private FighterListBox flb = new FighterListBox();
	private Panel tilePanel;

	/**
	 * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
	 */
	@Override
	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			@Override
			public void onUncaughtException(Throwable e) {
				log.log(Level.SEVERE, e.getMessage(), e);
				Window.Location.replace("/over_quota.html");
			}
		});
		LookupController.getInstance();
		final Timer t = new Timer() {
			@Override
			public void run() {
				if (LookupController.getInstance().isDLComplete()) {
					Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
						@Override
						public void execute() {
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
		String userAgent = Window.Navigator.getUserAgent();
		if (userAgent.contains("MSIE 7.") || userAgent.contains("MSIE 6.")) {
			Window.alert("This application will not work with any verison of IE below 8.\n"
					+ "Please upgrade to a more modern browser such as Chrome, FireFox, or IE8.\n"
					+ "The application works best in Chrome.");
			return;
		}
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
		loginService.login(GWT.getHostPageBaseURL() + "loggedin.jsp",
				GWT.getHostPageBaseURL() + "loggedin.jsp?trgt=goodbye",
				new AsyncCallback<LoginInfo>() {
					@Override
					public void onFailure(Throwable error) {
						Window.alert("Error in contacting the server, try later");
					}

					@Override
					public void onSuccess(LoginInfo loginInfo) {
						SecurityFactory.setLoginInfo(loginInfo);
						if (security.isLoggedIn()) {
							final Label hello;
							if (loginInfo.getScaName() == null || loginInfo.getScaName().trim().isEmpty()) {
								hello = new Label("Welcome " + loginInfo.getNickname());
							} else {
								hello = new Label("Welcome " + loginInfo.getScaName());
							}
							hello.setStyleName("hello", true);
							tilePanel.add(hello);
						}

						Shout shout = Shout.getInstance();
						shout.tell("loading");
						buildIndexPage();
						shout.hide();
					}
				});

		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				String historyToken = event.getValue();
				try {
					if (historyToken.substring(0, 8).equals("display:")) {
						String display = historyToken.substring(8);
						// Select the specified tab panel
						DisplayUtils.changeDisplay(DisplayUtils.Displays.valueOf(display));
					} else {
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

		SearchBar searchBar = new SearchBar();
		searchBar.addHandler(fighterFormWidget, EditViewEvent.TYPE);
		searchBar.addHandler(flb, SearchEvent.TYPE);
		searchBar.addHandler(searchBar, SearchEvent.TYPE);
		fighterFormWidget.addHandler(searchBar, DataUpdatedEvent.TYPE);
		tilePanel.add(searchBar);


		onIndexPage();
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onFailure(Throwable reason) {
				log.log(Level.SEVERE, reason.getMessage(), reason);
			}

			@Override
			public void onSuccess() {
				foundMultibleResults();
				buildFighterForm();
			}
		});
	}

	private void onIndexPage() {
		Panel signupPanel = new FlowPanel();
		signupPanel.setStyleName("dataBox");
		signupPanel.getElement().setId(DisplayUtils.Displays.SignupForm.toString());

		Panel innerSignupPanel = new FlowPanel();
		innerSignupPanel.setStyleName("dataBody");
		innerSignupPanel.setStyleName("signupPanel", true);

		HTML p = new HTML();
		p.setHTML("<a href=\"https://docs.google.com/spreadsheet/viewform?formkey=dGNDV2NYdGUtZk1aZXN6MURkaWlFNlE6MQ#gid=0\">Not registered? Sign up now!</a>");
		p.getElement().getStyle().setDisplay(Style.Display.INLINE);
		innerSignupPanel.add(p);

		Anchor gettingStarted = new Anchor("Getting Started>>");
		gettingStarted.setHref(GETTING_STARTED);
		gettingStarted.setStyleName("gettingStarted");
		innerSignupPanel.add(gettingStarted);

		if (!security.isLoggedIn()) {
			HTML p2 = new HTML();
			p2.setText("Registering with Falcon allows you review your own authorizations, "
					+ "update your contact information, and print your own fighter card at home.");

			HTML form = new HTML("<iframe src=\"https://docs.google.com/spreadsheet/embeddedform?formkey=dGNDV2NYdGUtZk1aZXN6MURkaWlFNlE6MQ\" "
					+ "width=\"620\" height=\"820\" frameborder=\"0\" marginheight=\"0\" marginwidth=\0\">Loading Signup Form...</iframe>");
			innerSignupPanel.add(p2);
			innerSignupPanel.add(form);
		}

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
