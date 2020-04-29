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
import com.ryhma6.maven.steambeater.model.DatabaseController;
import com.ryhma6.maven.steambeater.model.LanguageProvider;
import com.ryhma6.maven.steambeater.model.LoadingStatus;
import com.ryhma6.maven.steambeater.model.ObservableLoadingStatus;
import com.ryhma6.maven.steambeater.model.SteamAPICalls;
import com.ryhma6.maven.steambeater.model.TimeConverter;
import com.ryhma6.maven.steambeater.model.UserPreferences;
import com.ryhma6.maven.steambeater.model.steamAPI.GameData;
import com.ryhma6.maven.steambeater.model.steamAPI.PlayerProfile;
import com.ryhma6.maven.steambeater.view.GameListController;
import com.ryhma6.maven.steambeater.view.ProfileController;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
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
	private Button btnClose;

	/**
	 * Button to minimize the app
	 */
	@FXML
	private Button btnMinimize;

	/**
	 * Button to fullscreen the app
	 */
	@FXML
	private Button btnFull;

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
	 * Icon for the chosen UI language
	 */
	@FXML
	private ImageView languageIcon;

	/**
	 * ComboBox for the choice of UI language
	 */
	@FXML
	private ComboBox<?> languageChoice;
	
	@FXML
	private Label profileLabel;
	
	@FXML
	private Button profileImage;
	
	private DatabaseController db = DatabaseController.getInstance();
	
	private ProfileController profileController;
	private GameListController gameListController;

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
					mainApp.reloadData();
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
		mainApp.reloadData();
	}

	/**
	 * Loads Steam API data and refreshes UI
	 */
	@FXML
	private void refreshData() {
		if (UserPreferences.getSteamID() != null)
			mainApp.reloadData();
	}

	/**
	 * Used by logout button. Clears saved steamID from preferences and starts
	 * clearing data from UI.
	 */
	@FXML
	private void logout() {
		Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to logout?", ButtonType.YES,
				ButtonType.CANCEL);
		alert.showAndWait();
		if (alert.getResult() == ButtonType.YES) {
			UserPreferences.setSteamID("null");
			mainApp.resetSteamAPIData();
			loginButton.setManaged(true);
			loginButton.setVisible(true);
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
		if (event.getSource() == btnClose) {
			System.exit(0);
		} else if (event.getSource() == btnMinimize) {
			Stage primaryStage = (Stage) btnClose.getScene().getWindow();
			primaryStage.setIconified(true);
		} else if (event.getSource() == btnFull) {
			Stage primaryStage = (Stage) btnClose.getScene().getWindow();
			if (primaryStage.isMaximized()) {
				primaryStage.setMaximized(false);
				ImageView image = new ImageView("/maximize_button_64px.png");
				btnFull.setGraphic(image);
				image.setFitHeight(25);
				image.setFitWidth(25);
			} else {
				primaryStage.setMaximized(true);
				ImageView image = new ImageView("/restore_down_64px.png");
				btnFull.setGraphic(image);
				image.setFitHeight(25);
				image.setFitWidth(25);
			}
		}
	}
	
	private void initLanguageChoice() {
		languageChoice.getSelectionModel().clearSelection();	
		LanguageProvider langProv = LanguageProvider.getInstance();
		
		switch(langProv.getCurrentLocale().getLanguage()) {
			case "en":
				languageIcon.setImage(new Image("/img/UK.png"));
				languageChoice.getSelectionModel().select(0);
				break;
			case "fi":
				languageIcon.setImage(new Image("/img/finland.png"));
				languageChoice.getSelectionModel().select(1);
				break;
		}
		
		languageChoice.getSelectionModel().selectedItemProperty().addListener(obs -> {
			// sorting in alphabetical order
			if (languageChoice.getSelectionModel().getSelectedIndex() == 0) {
				languageIcon.setImage(new Image("/img/UK.png"));
				langProv.setLanguage("en", "GB");
			} else if (languageChoice.getSelectionModel().getSelectedIndex() == 1) {
				languageIcon.setImage(new Image("/img/finland.png"));
				langProv.setLanguage("fi", "FI");
			}
			mainApp.loadUI();
		});
	}
	
	@FXML
	private void handleProfileImageClicked(MouseEvent arg0) {
		profileController.openProfile();
	}
	

	/**
	 * Runs when application is started, sets steam's login image to the login
	 * button
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		profileImage.setManaged(false);
		profileImage.setVisible(false);
		
		profileLabel.setManaged(false);
		profileLabel.setVisible(false);

		initLanguageChoice();
		ImageView fullImage = new ImageView("/img/maximize_button_64px.png");
		btnFull.setGraphic(fullImage);
		fullImage.setFitHeight(25);
		fullImage.setFitWidth(25);

		ImageView minimizeImage = new ImageView("/img/minimize_window_64px.png");
		btnMinimize.setGraphic(minimizeImage);
		minimizeImage.setFitHeight(25);
		minimizeImage.setFitWidth(25);

		ImageView closeImage = new ImageView("/img/close_window_64px.png");
		btnClose.setGraphic(closeImage);
		closeImage.setFitHeight(25);
		closeImage.setFitWidth(25);

		ImageView loginImage = new ImageView("/img/steamicon.png");
		loginButton.setGraphic(loginImage);
		loginButton.setPadding(Insets.EMPTY);
		loginImage.setPreserveRatio(true);
		loginImage.setFitWidth(200);

		ImageView loginTestImage = new ImageView("/img/enter.png");
		signTestButton.setGraphic(loginTestImage);
		Tooltip loginTip = new Tooltip();
		loginTip.setText("Test gamelist");
		signTestButton.setTooltip(loginTip);
		loginTestImage.setFitHeight(35);
		loginTestImage.setFitWidth(35);

		ImageView exitImage = new ImageView("/img/exit.png");
		logoutButton.setGraphic(exitImage);
		Tooltip exitTip = new Tooltip();
		exitTip.setText("logout");
		logoutButton.setTooltip(exitTip);
		exitImage.setFitHeight(35);
		exitImage.setFitWidth(35);

		ImageView refreshImage = new ImageView("/img/refresh.png");
		Tooltip refreshTip = new Tooltip();
		refreshTip.setText("Refresh");
		refreshButton.setTooltip(refreshTip);
		refreshButton.setGraphic(refreshImage);
		refreshImage.setFitHeight(35);
		refreshImage.setFitWidth(35);

		loadStateLabel.setPadding(new Insets(10, 0, 0, 0));

		ObjectProperty<PlayerProfile> userProfile = SteamAPICalls.getSignedPlayerProfile();
		userProfile.addListener(obs -> {
			try {
				ImageView avatar = new ImageView(userProfile.get().getAvatar());
				profileImage.setGraphic(avatar);
				profileLabel.setText(userProfile.get().getPersonaname());
			}catch(Exception e) {
				profileLabel.setManaged(true);
				profileLabel.setVisible(true);
				profileLabel.setText("Not logged in");
			}
		});
		
		ObservableLoadingStatus stateObject = ObservableLoadingStatus.getInstance();
		ObjectProperty<LoadingStatus> stateProperty = stateObject.getLoadingStateProperty();
		loadStateLabel.setText(LoadingStatus.getDescription(stateProperty.getValue()));
		logoutButton.setDisable(true);
		refreshButton.setDisable(true);

		/*
		 * Add listener to load state and show load state description on UI. Disable
		 * sign, logout- and refresh-buttons when data loading is still in progress.
		 */
		stateProperty.addListener(obs -> {
			loadStateLabel.setText(LoadingStatus.getDescription(stateProperty.getValue()));
			if (stateProperty.getValue() == LoadingStatus.PRELOAD) {
				loginButton.setDisable(false);
				signTestButton.setDisable(false);
				logoutButton.setDisable(true);
				refreshButton.setDisable(true);
			} else if (stateProperty.getValue() == LoadingStatus.COMPLETED) {
				loginButton.setDisable(false);
				loginButton.setVisible(false);
				loginButton.setManaged(false);
				signTestButton.setDisable(false);
				logoutButton.setDisable(false);
				refreshButton.setDisable(false);
				profileImage.setVisible(true);
				profileImage.setManaged(true);
				profileLabel.setManaged(true);
				profileLabel.setVisible(true);
				loadStateLabel.setText(loadStateLabel.getText() + ": "
						+ TimeConverter.epochMillisToLocalTimestamp(stateObject.getLastCompletionMillis()));
			} else if (stateProperty.getValue() == LoadingStatus.FAILURE) {
				loginButton.setDisable(false);
				signTestButton.setDisable(false);
				logoutButton.setDisable(false);
				refreshButton.setDisable(false);
			} else {
				loginButton.setDisable(true);
				signTestButton.setDisable(true);
				logoutButton.setDisable(true);
				refreshButton.setDisable(true);
			}
			String errors = "";
			for(String msg: stateObject.getErrorMessages()) {
				errors+="\n";
				errors+=msg;
			}
			loadStateLabel.setText(loadStateLabel.getText()+errors);

		});
	}

	public void setProfileController(ProfileController controller) {
		this.profileController = controller;
	}
	
	public void setGameListController(GameListController controller) {
		this.gameListController = controller;
	}
}
