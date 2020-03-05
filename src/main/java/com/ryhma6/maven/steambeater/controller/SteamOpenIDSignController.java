package com.ryhma6.maven.steambeater.controller;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.expressme.openid.Association;
import org.expressme.openid.Endpoint;
import org.expressme.openid.OpenIdManager;

import com.ryhma6.maven.steambeater.model.UserPreferences;

import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SteamOpenIDSignController {

	private Stage newWindow;
	private UserPreferences prefs = new UserPreferences();

	@FXML
	private void handleBrowserButtonAction(ActionEvent event) {

		Label secondLabel = new Label("Steam OpenID login");

		// StackPane secondaryLayout = new StackPane();
		// secondaryLayout.getChildren().add(secondLabel);
		// Scene secondScene = new Scene(secondaryLayout, 230, 100);

		OpenIdManager manager = new OpenIdManager();
		// manager.setReturnTo("http://www.openid-example.com/openId");
		manager.setReturnTo("http://localhost:8080");
		manager.setRealm("http://localhost:8080");
		Endpoint endpoint = manager.lookupEndpoint("https://steamcommunity.com/openid");
		Association association = manager.lookupAssociation(endpoint);
		String url = manager.getAuthenticationUrl(endpoint, association);
		System.out.println("Copy the authentication URL in browser:\n" + url);

		WebView webView = new WebView();

		// webView.getEngine().load("http://google.com");
		webView.getEngine().load(url);

		webView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
			if (Worker.State.SUCCEEDED.equals(newValue)) {
				System.out.println(webView.getEngine().getLocation());
				getCookieUsingCookieHandler();
			}
		});

		VBox vBox = new VBox(webView);
		Scene secondScene = new Scene(vBox, 960, 600);

		// New window (Stage)
		newWindow = new Stage();
		newWindow.setTitle("Steam OpenID login");
		newWindow.setScene(secondScene);

		// Specifies the modality for new window.
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

	// Instantiate CookieManager;
	// make sure to set CookiePolicy
	CookieManager manager = new CookieManager();

	public void getCookieUsingCookieHandler() {
		try {

			manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
			CookieHandler.setDefault(manager);

			// get content from URLConnection;
			// cookies are set by web site
			URL url = new URL("https://steamcommunity.com/");
			URLConnection connection = url.openConnection();
			connection.getContent();

			// get cookies from underlying
			// CookieStore
			CookieStore cookieJar = manager.getCookieStore();
			List<HttpCookie> cookies = cookieJar.getCookies();
			for (HttpCookie cookie : cookies) {
				System.out.println("CookieHandler retrieved cookie: " + cookie);
				if (cookie.getName().equals("steamLoginSecure")) {
					System.out.println("SteamIDCookieValue:" + cookie.getValue());
					prefs.setSteamID(cookie.getValue().substring(0,17));
					newWindow.close();
				}
			}
		} catch (Exception e) {
			System.out.println("Unable to get cookie using CookieHandler");
			e.printStackTrace();
		}
	}
}
