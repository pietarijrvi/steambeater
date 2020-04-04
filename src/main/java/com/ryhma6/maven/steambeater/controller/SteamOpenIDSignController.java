package com.ryhma6.maven.steambeater.controller;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.ResourceBundle;

import org.expressme.openid.Association;
import org.expressme.openid.Endpoint;
import org.expressme.openid.OpenIdManager;

import com.ryhma6.maven.steambeater.MainApp;
import com.ryhma6.maven.steambeater.model.LoadingState;
import com.ryhma6.maven.steambeater.model.ObservableLoadingState;
import com.ryhma6.maven.steambeater.model.TimeConverter;
import com.ryhma6.maven.steambeater.model.UserPreferences;

import javafx.beans.property.ObjectProperty;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
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
public class SteamOpenIDSignController implements Initializable {

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
	 * Button to close the app
	 */
	@FXML
	private ImageView btnClose;

	/**
	 * Button to minimize the app
	 */
	@FXML
	private ImageView btnMinimize;

	/**
	 * Button to fullscreen the app
	 */
	@FXML
	private ImageView btnFull;

	/**
	 * HBox inside taskbar. This is where the window resize buttons are.
	 */
	@FXML
	private HBox taskBarHbox;

	/**
	 * Button for logging out of Steam
	 */
	@FXML
	private Button logoutButton;

	/**
	 * Button for testing Steam login
	 */
	@FXML
	private Button signTestButton;

	/**
	 * Button for refreshing the Steam API data
	 */
	@FXML
	private Button refreshButton;

	/**
	 * Displays info about the current data loading state
	 */
	@FXML
	private Label loadStateLabel;

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
		if (UserPreferences.getSteamID() != null)
			mainApp.loadSteamAPIData();
	}

	/**
	 * Used by logout button. Clears saved steamID from preferences and starts
	 * clearing data from UI.
	 */
	@FXML
	private void logout() {
		Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to logout?", ButtonType.YES, ButtonType.CANCEL);
		alert.showAndWait();
		if (alert.getResult() == ButtonType.YES) {
			UserPreferences.setSteamID("null");
			mainApp.resetSteamAPIData();
		}
		
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
	 * Handles topbars mouseclick event: close app, fullscreen, minimize.
	 * 
	 * @param event
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	@FXML
	private void handleMouseEvent(MouseEvent event) throws IOException, URISyntaxException {
		if(event.getSource() == btnClose) {
			Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to close the application?", ButtonType.YES, ButtonType.CANCEL);
			alert.showAndWait();
			if (alert.getResult() == ButtonType.YES) {
				System.exit(0);
			}
		} else if (event.getSource() == btnMinimize) {
			Stage primaryStage = (Stage) btnClose.getScene().getWindow();
			primaryStage.setIconified(true);
		} else if (event.getSource() == btnFull) {
			Stage primaryStage = (Stage) btnClose.getScene().getWindow();
			if (primaryStage.isMaximized()) {
				primaryStage.setMaximized(false);
				Image image = new Image("/maximize_button_64px.png");
				btnFull.setImage(image);
			} else {
				primaryStage.setMaximized(true);
				Image image = new Image("/restore_down_64px.png");
				btnFull.setImage(image);
			}
		}
	}

	/**
	 * Runs when application is started, sets steam's login image to the login
	 * button
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ImageView loginImage = new ImageView("/steamicon.png");
		loginButton.setGraphic(loginImage);
		loginButton.setPadding(Insets.EMPTY);

		ImageView loginTestImage = new ImageView("/enter.png");
		signTestButton.setGraphic(loginTestImage);
		Tooltip loginTip = new Tooltip();
		loginTip.setText("Test gamelist");
		signTestButton.setTooltip(loginTip);
		loginTestImage.setFitHeight(35);
		loginTestImage.setFitWidth(35);

		ImageView exitImage = new ImageView("/exit.png");
		logoutButton.setGraphic(exitImage);
		Tooltip exitTip = new Tooltip();
		exitTip.setText("logout");
		logoutButton.setTooltip(exitTip);
		exitImage.setFitHeight(35);
		exitImage.setFitWidth(35);

		ImageView refreshImage = new ImageView("/refresh.png");
		Tooltip refreshTip = new Tooltip();
		refreshTip.setText("Refresh");
		refreshButton.setTooltip(refreshTip);
		refreshButton.setGraphic(refreshImage);
		refreshImage.setFitHeight(35);
		refreshImage.setFitWidth(35);

		ObservableLoadingState stateObject = ObservableLoadingState.getInstance();
		ObjectProperty<LoadingState> stateProperty = stateObject.getLoadingStateProperty();
		loadStateLabel.setText(LoadingState.getDescription(stateProperty.getValue()));
		logoutButton.setDisable(true);
		refreshButton.setDisable(true);

		/*
		 * Add listener to load state and show load state description on UI. Disable
		 * sign, logout- and refresh-buttons when data loading is still in progress.
		 */
		stateProperty.addListener(obs -> {
			loadStateLabel.setText(LoadingState.getDescription(stateProperty.getValue()));
			if(stateProperty.getValue() == LoadingState.PRELOAD) {
				loginButton.setDisable(false);
				signTestButton.setDisable(false);
				logoutButton.setDisable(true);
				refreshButton.setDisable(true);
			}else if (stateProperty.getValue() == LoadingState.COMPLETED) {
				loginButton.setDisable(false);
				signTestButton.setDisable(false);
				logoutButton.setDisable(false);
				refreshButton.setDisable(false);
				loadStateLabel.setText(loadStateLabel.getText() + ": " + TimeConverter.epochMillisToLocalTimestamp(stateObject.getLastCompletionMillis()));
			} else {
				loginButton.setDisable(true);
				signTestButton.setDisable(true);
				logoutButton.setDisable(true);
				refreshButton.setDisable(true);
			}

		});
	}
}
