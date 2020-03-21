package com.ryhma6.maven.steambeater.controller;

import java.io.File;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.ResourceBundle;

import org.expressme.openid.Association;
import org.expressme.openid.Endpoint;
import org.expressme.openid.OpenIdManager;

import com.ryhma6.maven.steambeater.MainApp;
import com.ryhma6.maven.steambeater.model.UserPreferences;

import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The class SteamOpenIDSignController controls the Steam OpenID sign process
 * and is used by the FXML that contains the sign buttons. User signs using
 * Steam OpenID form in embedded browser (this app won't handle the username or
 * password). After successful login the SteamID is retrieved from a cookie.
 */
public class SteamOpenIDSignController implements Initializable{

	/**
	 * Reference to main app, set after loading the FXML that uses this controller.
	 */
	private MainApp mainApp;
	/**
	 * Stage that contains embedded browser (popup).
	 */
	private Stage newWindow;
	/**
	 * Cookiemanager used for storing steamcommunity cookies. Reseted for each
	 * login.
	 */
	private CookieManager cookieManager;
	
	/**
	 * Button to login into Steam
	 */
	@FXML
	private Button loginButton;

	/**
	 * Login button action (FXML). Opens new window containing embedded browser,
	 * opens Steam OpenID login page. SteamID retrieved from cookies after
	 * successful login.
	 * 
	 * @param event Click event
	 */
	@FXML
	private void handleLoginButtonAction(ActionEvent event) {
		cookieManager = new CookieManager();
		cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
		CookieHandler.setDefault(cookieManager);

		OpenIdManager manager = new OpenIdManager();
		/*
		 * return address and realm not actually used here (no redirection after
		 * successful login), required parameters for Steam OpenID
		 */
		manager.setReturnTo("http://localhost:8080");
		manager.setRealm("http://localhost:8080");
		Endpoint endpoint = manager.lookupEndpoint("https://steamcommunity.com/openid");
		Association association = manager.lookupAssociation(endpoint);
		String url = manager.getAuthenticationUrl(endpoint, association);

		WebView webView = new WebView();
		webView.getEngine().load(url);

		/*
		 * Check cookies on every state change (web navigation/redirects) and try to //
		 * find the cookie containing steamID. Keeps listening until the cookie is
		 * found.
		 */
		webView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
			if (Worker.State.SUCCEEDED.equals(newValue)) {
				getSteamIDFromCookie();
			}
		});

		VBox vBox = new VBox(webView);
		Scene secondScene = new Scene(vBox, 960, 600);

		// New window (Stage)
		newWindow = new Stage();
		newWindow.setTitle("Steam OpenID login");
		newWindow.setScene(secondScene);

		// Specifies the modality for new window - login requires modal window
		newWindow.initModality(Modality.WINDOW_MODAL);

		// Specifies the owner Window (parent) for new window
		Node source = (Node) event.getSource();
		Stage parentStage = (Stage) source.getScene().getWindow();
		newWindow.initOwner(parentStage);

		// Set position of second window, related to primary window.
		newWindow.setX(parentStage.getX() + 200);
		newWindow.setY(parentStage.getY() + 100);

		newWindow.show();
	}

	/**
	 * Search steamcommunity cookies and look for steamLoginSecure is found
	 * (contains steamID). Close browser window and continue to game data loading
	 * when steamLoginSecure cookie is found. steamLoginSecure cookie is retrieved
	 * after successful signing.
	 */
	private void getSteamIDFromCookie() {
		try {
			// get content from URLConnection, cookies are set by web site
			URL url = new URL("https://steamcommunity.com/");
			URLConnection connection = url.openConnection();
			connection.getContent();

			// get cookies from underlying CookieStore
			CookieStore cookieJar = cookieManager.getCookieStore();
			List<HttpCookie> cookies = cookieJar.getCookies();
			for (HttpCookie cookie : cookies) {
				if (cookie.getName().equals("steamLoginSecure")) {
					System.out.println("SteamIDCookieValue:" + cookie.getValue());
					// save retrieved SteamID to preferences
					UserPreferences.setSteamID(cookie.getValue().substring(0, 17));
					newWindow.close();
					mainApp.loadSteamAPIData();
				}
			}
		} catch (Exception e) {
			System.out.println("Unable to get cookie.");
			e.printStackTrace();
		}
	}

	/**
	 * Used by test sign button. Used for testing, saves predefined steamID to
	 * preferences and uses it to start loading Steam API data.
	 */
	@FXML
	private void loadWithTestValues() {
		UserPreferences.setSteamID("76561197960505737");
		mainApp.loadSteamAPIData();
	}
	
	/**
	 * Loads Steam API data and refreshes UI
	 */
	@FXML
	private void refreshData() {
		if(UserPreferences.getSteamID()!=null)
			mainApp.loadSteamAPIData();
	}

	/**
	 * Used by logout button. Clears saved steamID from preferences and starts
	 * clearing data from UI.
	 */
	@FXML
	private void logout() {
		UserPreferences.setSteamID("null");
		mainApp.resetSteamAPIData();
	}

	/**
	 * Sets main app reference, has to be set immediately after loading the FXML
	 * that uses this controller.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	/**
	 * Runs when application is started, sets steam's login image to the login button
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ImageView loginImage = new ImageView("/steamicon.png");
        loginButton.setGraphic(loginImage);
        loginButton.setPadding(Insets.EMPTY);
	}
}
