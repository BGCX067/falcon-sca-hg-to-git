package org.sca.calontir.cmpe.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
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
		String userAgent = Window.Navigator.getUserAgent();
		if (userAgent.contains("MSIE 7.") || userAgent.contains("MSIE 6.")) {
			Window.alert("This application will not work with any verison of IE below 8.\n"
					+ "Please upgrade to a more modern browser such as Chrome, FireFox, or IE8.\n"
					+ "The application works best in Chrome.");
			return;
		}
		tilePanel = RootPanel.get("tile");
		LookupController.getInstance();

		Image titleImage = new Image();
		titleImage.setUrl("images/title_image.gif");
		titleImage.addStyleName("titleImage");
		tilePanel.add(titleImage);

		Label titleLabel = new Label("FALCON");
		titleLabel.setWordWrap(false);
		titleLabel.setTitle("Fighter Authorization List Calontir ONline (FALCON)");
		titleLabel.setStyleName("title");

		Label betaLabel = new Label("beta");
		betaLabel.setWordWrap(false);
		betaLabel.getElement().getStyle().setColor("red");
		betaLabel.getElement().getStyle().setVerticalAlign(Style.VerticalAlign.TOP);
		betaLabel.getElement().getStyle().setFontSize(50.0, Style.Unit.PCT);
		betaLabel.getElement().getStyle().setDisplay(Style.Display.INLINE);



		tilePanel.add(titleLabel);
		tilePanel.add(betaLabel);
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

				buildIndexPage();
			}
		});

		//Must turn off search button and have the load data started above to turn it back on.
	}

	private void buildIndexPage() {
		// remove Loading-Message from page
		tilePanel.getElement().removeChild(DOM.getElementById("Loading-Message"));


		CalonBar calonBar = new CalonBar();
		tilePanel.add(calonBar);

		SearchBar searchBar = new SearchBar();
		searchBar.addHandler(fighterFormWidget, EditViewEvent.TYPE);
		searchBar.addHandler(flb, SearchEvent.TYPE);
		fighterFormWidget.addHandler(searchBar, DataUpdatedEvent.TYPE);
		fighterFormWidget.addHandler(flb, SearchEvent.TYPE);
		tilePanel.add(searchBar);


		onIndexPage();
		foundMultibleResults();
		buildFighterForm();

	}

	private void onIndexPage() {
		Panel signupPanel = new FlowPanel();
		signupPanel.setStyleName("dataBox");
		signupPanel.getElement().setId("Signup-Form");

		Panel innerSignupPanel = new FlowPanel();
		innerSignupPanel.setStyleName("dataBody");
		innerSignupPanel.setStyleName("signupPanel", true);

		signupPanel.add(innerSignupPanel);

		HTML p = new HTML();
		p.setHTML("<a href=\"https://docs.google.com/spreadsheet/viewform?formkey=dGNDV2NYdGUtZk1aZXN6MURkaWlFNlE6MQ#gid=0\">Not registered? Sign up now!</a>");
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

		tilePanel.add(signupPanel);

	}

	private void foundMultibleResults() {
		flb.addHandler(fighterFormWidget, EditViewEvent.TYPE);
		tilePanel.add(flb);
	}

	private void buildFighterForm() {
		final FormPanel fighterForm = new FormPanel();
		fighterForm.setAction("/FighterServlet");
		fighterForm.getElement().setId("FighterForm");
		fighterForm.getElement().getStyle().setDisplay(Style.Display.NONE);
		fighterForm.setMethod(FormPanel.METHOD_POST);

		fighterFormWidget.setForm(fighterForm);
		fighterForm.add(fighterFormWidget);
		fighterForm.addSubmitHandler(fighterFormWidget);
		fighterForm.addSubmitCompleteHandler(fighterFormWidget);


		tilePanel.add(fighterForm);
	}
}
