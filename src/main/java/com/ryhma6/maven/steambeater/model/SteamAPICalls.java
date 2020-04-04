package com.ryhma6.maven.steambeater.model;

import java.io.IOException;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.ryhma6.maven.steambeater.model.steamAPI.Achievement;
import com.ryhma6.maven.steambeater.model.steamAPI.Friend;
import com.ryhma6.maven.steambeater.model.steamAPI.FriendList;
import com.ryhma6.maven.steambeater.model.steamAPI.GameData;
import com.ryhma6.maven.steambeater.model.steamAPI.GameStatistics;
import com.ryhma6.maven.steambeater.model.steamAPI.OwnedGames;
import com.ryhma6.maven.steambeater.model.steamAPI.PlayerProfile;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Class SteamAPICalls handles communication with SteamAPI (documentation:
 * https://developer.valvesoftware.com/wiki/Steam_Web_API). API usage includes
 * retrieving games used by players, friend list and friend information,
 * achievements and user statistics. API JSON responses are mapped to objects.
 *
 */
public class SteamAPICalls {

	/**
	 * UI game list is formed from the data in this list.
	 */
	private static ObservableList<GameData> playerGames = FXCollections.observableArrayList();
	/**
	 * UI friend list is formed from the data in this list.
	 */
	private static ObservableList<Friend> friendList = FXCollections.observableArrayList();
	/**
	 * UI friend game info (detail/comparison is formed from the data in this list.
	 */
	private static ObservableList<GameData> friendsGames = FXCollections.observableArrayList();
	/**
	 * Games are mapped by game id for easier access to game data when combining
	 * data from multiple api calls and database.
	 */
	private Map<Integer, GameData> gamesMappedByGameID = new HashMap<Integer, GameData>();
	/**
	 * Games are mapped by game id for easier access to friend game data when
	 * combining data from multiple api calls and database.
	 */
	private Map<Integer, GameData> fGamesMappedByGameID = new HashMap<Integer, GameData>();

	/**
	 * Jackson JSON mapper
	 */
	private ObjectMapper mapper = new ObjectMapper();
	/**
	 * API key used for Steam api usage authorisation. Included in every API call.
	 */
	private static final String API_KEY = "706A9896DF2A6C97072D53C86FCE29A3";

	public SteamAPICalls() {
		mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
	}

	/**
	 * Returns player's saved SteamID.
	 * 
	 * @return steamID
	 */
	private String getSteamID() {
		return UserPreferences.getSteamID();
	}

	public List<GameData> getLoadedGames() {
		List<GameData> games = new ArrayList<GameData>(gamesMappedByGameID.values());
		return games;
	}

	/**
	 * Returns the ObservableList that contain game data retrieved from SteamAPI,
	 * used for forming UI game list.
	 * 
	 * @return ObservableList
	 */
	public static ObservableList<GameData> getOwnedGames() {
		return playerGames;
	}

	/**
	 * Returns the ObservableList that contain friend data retrieved from SteamAPI,
	 * used for forming UI friend list.
	 * 
	 * @return ObservableList
	 */
	public static ObservableList<Friend> getFriendList() {
		return friendList;
	}

	/**
	 * Returns the ObservableList that contain friend game data retrieved from
	 * SteamAPI, used for UI stat comparison.
	 * 
	 * @return ObservableList
	 */
	public static ObservableList<GameData> getFriendsGames() {
		return friendsGames;
	}

	/**
	 * Modify the current incomplete gamelist (from SteamAPI) to include the data
	 * (game status info; ignored, unbeatable, beaten) saved in database.
	 * 
	 * @param dbEntries game data retrieved from database
	 */
	public void setSavedSelections(List<GameListEntry> dbEntries) {
		try {
			List<GameListEntry> savedSelections = dbEntries;
			for (GameListEntry g : savedSelections) {
				int gameID = g.getGameID();
				GameData gameData = gamesMappedByGameID.get(gameID);
				gameData.setIgnored(g.getIgnored());
				gameData.setBeaten(g.getBeaten());
				gameData.setUnbeatable(g.getUnbeatable());
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Setting db selections failed - no selections or no game list from Steam");
		}
		setGamesToUI();
	}

	public void loadGamesFromDatabase(List<GameListEntry> dbEntries) {
		try {
			for (GameListEntry g : dbEntries) {
				int gameID = g.getGameID();
				GameData gameData = new GameData(); // gamesMappedByGameID.get(gameID);
				gameData.setIgnored(g.getIgnored());
				gameData.setBeaten(g.getBeaten());
				gameData.setUnbeatable(g.getUnbeatable());
				gameData.setImg_logo_url(g.getLogoImageUrl());
				gameData.setName(g.getName());
				gameData.setPlaytime_forever(g.getPlaytimeForever());
				gameData.setAppid(g.getGameID());
				gamesMappedByGameID.put(gameID, gameData);
			}
		} catch (Exception e) {
			System.out.println("Setting db games failed");
		}
		setGamesToUI();
		System.out.println("Full game list loaded from database");
	}

	/**
	 * Set game data to observableList used in UI game list. Operation is made in UI
	 * thread.
	 */
	private void setGamesToUI() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						playerGames.addAll(new ArrayList<GameData>(gamesMappedByGameID.values()));
					}
				});
			}
		}).start();
	}

	/**
	 * Clear observableLists that are also used in UI lists. Operation is made in UI
	 * thread.
	 */
	public void resetItems() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						playerGames.clear();
						friendList.clear();
						friendsGames.clear();
					}
				});
			}
		}).start();
	}

	/**
	 * Loads player's game data (owned games) from SteamAPI and maps the response
	 * JSON data to object. Successful loading returns true;
	 */
	public Boolean loadSteamGames() {
		Boolean returnValue = false;
		List<GameData> playerGamesTemp = new ArrayList<GameData>();
		resetItems();
		gamesMappedByGameID.clear();
		URL url;
		HttpURLConnection con;
		try {
			url = new URL("http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=" + API_KEY + "&steamid="
					+ getSteamID() + "&include_appinfo=1&include_played_free_games=1&format=json");
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json");
			int responsecode = con.getResponseCode();

			if (responsecode == 200) {
				String str = "";
				Scanner sc = new Scanner(url.openStream());
				while (sc.hasNext()) {
					str += sc.nextLine();
				}
				sc.close();

				// JSON string to Java Object
				OwnedGames games = mapper.readValue(str, OwnedGames.class);
				try {
					playerGamesTemp.addAll(games.getGames());
					for (GameData g : playerGamesTemp) {
						gamesMappedByGameID.put(g.getAppid(), g);
					}
				} catch (Exception e) {
					System.out.println("SteamAPI: loading gamelist failed");
				}
				returnValue = true;
				System.out.println("Owned games (SteamAPI): " + games.getGame_count());
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return returnValue;
	}

	/**
	 * Loads player's friend info from SteamAPI and maps the response JSON data to
	 * object.
	 */
	public void loadSteamFriends() {
		List<Friend> friendListTemp = new ArrayList<Friend>();
		URL url;
		HttpURLConnection con;
		List<Friend> friendIdList = new ArrayList<Friend>();
		try {
			url = new URL("http://api.steampowered.com/ISteamUser/GetFriendList/v0001/?key=" + API_KEY + "&steamid="
					+ getSteamID() + "&relationship=friend");
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json");
			int responsecode = con.getResponseCode();

			if (responsecode == 200) {
				String str = "";
				Scanner sc = new Scanner(url.openStream());
				while (sc.hasNext()) {
					str += sc.nextLine();
				}
				sc.close();

				// JSON string to Java Object
				FriendList response = mapper.readValue(str, FriendList.class);
				friendIdList.addAll(response.getFriends());
				System.out.println("Friends (SteamAPI): " + friendIdList.size());
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		List<String> friendSteamIDList = new ArrayList<>();

		for (Friend f : friendIdList) {
			friendSteamIDList.add(f.getSteamid());
		}

		// friend list has to be split for requesting profile info (Steam api allows max
		// 100 per request)
		int requiredSplitLists = (int) Math.ceil(friendSteamIDList.size() / 100f);

		Map<String, PlayerProfile> profiles = new HashMap<String, PlayerProfile>();
		for (int i = 0; i < requiredSplitLists; i++) {
			List<String> friendSubList;
			if (i < (requiredSplitLists - 1))
				friendSubList = friendSteamIDList.subList(i * 100, i * 100 + 100);
			else
				friendSubList = friendSteamIDList.subList(i * 100, friendSteamIDList.size());
			profiles.putAll(loadSteamPlayerProfiles(friendSubList));
		}

		for (Friend f : friendIdList) {
			PlayerProfile profile = profiles.get(f.getSteamid());
			if (profile != null) {
				f.setPlayerProfile(profile);
				friendListTemp.add(f);
			}
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						friendList.addAll(friendListTemp);
					}
				});
			}
		}).start();
	}

	/**
	 * Loads profile info from SteamAPI.
	 * 
	 * @param steamIDList
	 * @return
	 */
	private Map<String, PlayerProfile> loadSteamPlayerProfiles(List<String> steamIDList) {
		URL url;
		HttpURLConnection con;
		Map<String, PlayerProfile> profileMap = new HashMap<String, PlayerProfile>();
		try {
			String separatedIDList = String.join(",", steamIDList);
			String parameters = String.format("key=%s&steamids=%s", API_KEY, separatedIDList);
			url = new URL("http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?" + parameters);

			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json");
			int responsecode = con.getResponseCode();

			if (responsecode == 200) {
				String str = "";
				Scanner sc = new Scanner(url.openStream());
				while (sc.hasNext()) {
					str += sc.nextLine();
				}
				sc.close();

				// JSON string to Java Object
				JsonNode rootNode = new ObjectMapper().readTree(new StringReader(str));
				JsonNode innerNode = rootNode.get("response");
				ObjectReader objectReader = mapper.readerFor(new TypeReference<List<PlayerProfile>>() {
				}).withRootName("players");
				List<PlayerProfile> profiles = objectReader.readValue(innerNode);

				for (PlayerProfile p : profiles) {
					profileMap.put(p.getSteamid(), p);
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return profileMap;
	}

	/**
	 * Loads the game schema (achievements belonging to game etc.) from SteamAPI.
	 * Game info is added to previously retrieved game data.
	 * 
	 * @param appID
	 */
	public void loadGameSchema(int appID) {
		URL url;
		HttpURLConnection con;
		try {
			String parameters = String.format("key=%s&appid=%s", API_KEY, appID);
			url = new URL(" http://api.steampowered.com/ISteamUserStats/GetSchemaForGame/v2/?" + parameters);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json");
			int responsecode = con.getResponseCode();

			if (responsecode == 200) {
				String str = "";
				Scanner sc = new Scanner(url.openStream());
				while (sc.hasNext()) {
					str += sc.nextLine();
				}
				sc.close();

				// JSON string to Java Object
				mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false);
				JsonNode rootNode = new ObjectMapper().readTree(new StringReader(str));
				JsonNode innerNode = rootNode.path("game").path("availableGameStats");
				ObjectReader objectReader = mapper.readerFor(new TypeReference<GameStatistics>() {
				});
				mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
				try {
					GameStatistics gameStats = objectReader.readValue(innerNode);
					gamesMappedByGameID.get(appID).setGameStatistics(gameStats);
					getAchievementCompletionInfo(appID);
				} catch (Exception e) {
					System.out.println("This game has no achievements");
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Loads the achievement completion info (completion and timestamp) for a game
	 * defined by appID from SteamAPI. Info is added to previously retrieved game
	 * data.
	 * 
	 * @param appID
	 */
	private void getAchievementCompletionInfo(int appID) {
		URL url;
		HttpURLConnection con;
		try {
			String parameters = String.format("key=%s&appid=%s&steamid=%s", API_KEY, appID, getSteamID());
			url = new URL(" http://api.steampowered.com/ISteamUserStats/GetPlayerAchievements/v0001/?" + parameters);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json");
			int responsecode = con.getResponseCode();

			if (responsecode == 200) {
				String str = "";
				Scanner sc = new Scanner(url.openStream());
				while (sc.hasNext()) {
					str += sc.nextLine();
				}
				sc.close();

				// JSON string to Java Object
				mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false);

				JsonNode rootNode = new ObjectMapper().readTree(new StringReader(str));
				JsonNode innerNode = rootNode.path("playerstats");
				ObjectReader objectReader = mapper.readerFor(new TypeReference<GameStatistics>() {
				});
				GameStatistics achievementCompletionInfo = objectReader.readValue(innerNode);
				mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
				// Mapping achievement schema of the requested game, using name (not display
				// name) as identifier
				Map<String, Achievement> achievements = new HashMap<String, Achievement>();
				for (Achievement ach : gamesMappedByGameID.get(appID).getGameStatistics().getAchievements()) {
					achievements.put(ach.getName(), ach);
				}

				// Filling the completion info from this API request to the achievement schema
				// of the game
				for (Achievement a : achievementCompletionInfo.getAchievements()) {
					Achievement achievementSchema = achievements.get(a.getApiname());
					achievementSchema.setAchieved(a.getAchieved());
					achievementSchema.setUnlocktime(a.getUnlocktime());
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	// TODO: METHOD IN PROGRESS
	public void loadFriendsGames(String friendsID) {
		List<GameData> friendsGamesTemp = new ArrayList<GameData>(); // Used for a WIP method

		friendsGames.clear();
		fGamesMappedByGameID.clear();
		URL url;
		HttpURLConnection con;

		try {
			url = new URL("http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=" + API_KEY + "&steamid="
					+ friendsID + "&include_appinfo=1&include_played_free_games=1&format=json");
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json");
			int responsecode = con.getResponseCode();

			if (responsecode == 200) {
				String str = "";
				Scanner sc = new Scanner(url.openStream());
				while (sc.hasNext()) {
					str += sc.nextLine();
				}
				sc.close();

				// JSON string to Java Object
				OwnedGames games = mapper.readValue(str, OwnedGames.class);

				try {
					friendsGamesTemp.addAll(games.getGames());
					for (GameData g : friendsGamesTemp) {
						fGamesMappedByGameID.put(g.getAppid(), g);
					}
				} catch (Exception e) {
					System.out.println("SteamAPI: loading gamelist failed");
				}

				System.out.println("Friend's games: " + games.getGame_count());

			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		friendsGames.addAll(friendsGamesTemp);
		/*
		 * new Thread(new Runnable() {
		 * 
		 * @Override public void run() { Platform.runLater(new Runnable() {
		 * 
		 * @Override public void run() { friendsGames.addAll(friendsGamesTemp); } }); }
		 * }).start();
		 */
	}
}
