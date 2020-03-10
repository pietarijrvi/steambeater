package com.ryhma6.maven.steambeater.model;

import java.io.IOException;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
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

public class SteamAPICalls {
	private List<GameData> playerGamesTemp = new ArrayList<GameData>();
	private List<Friend> friendListTemp = new ArrayList<Friend>();
	private List<GameData> friendsGamesTemp = new ArrayList<GameData>();
	
	private static ObservableList<GameData> playerGames = FXCollections.observableArrayList();
	private static ObservableList<Friend> friendList = FXCollections.observableArrayList();
	private static ObservableList<GameData> friendsGames = FXCollections.observableArrayList();
	private Map<Integer,GameData> gamesMappedByGameID = new HashMap<Integer,GameData>();
	private Map<Integer,GameData> fGamesMappedByGameID = new HashMap<Integer,GameData>();
	
	private ObjectMapper mapper = new ObjectMapper();
	private String apiKey = "38FB06680EA5CA6B526B31CBD4E43593";
	//private String steamID = "76561197960505737";
	private UserPreferences prefs = new UserPreferences(); 
	
	public SteamAPICalls() {
		mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
	}
	
	private String getSteamID() {
		return prefs.getSteamID();
	}
	
	public static ObservableList<GameData> getOwnedGames() {
		return playerGames;
	}
	
	public static ObservableList<Friend> getFriendList() {
		return friendList;
	}
	
	public static ObservableList<GameData> getFriendsGames() {
		return friendsGames;
	}
	
	public void setSavedSelections(List<GameListEntry> dbEntries) {
		try {
			List<GameListEntry> savedSelections = dbEntries;
			for(GameListEntry g: savedSelections) {
				int gameID = Integer.parseInt(g.getGameID());
				GameData gameData = gamesMappedByGameID.get(gameID);
				//System.out.println("from DB: " + gameData.getName() + "ignored: " + g.getIgnored());
				gameData.setIgnored(g.getIgnored());
				gameData.setBeaten(g.getBeaten());
				gameData.setUnbeatable(g.getUnbeatable());
			}
		}catch(Exception e) {
			System.out.println("Setting db selections failed - no selections or no game list from Steam");
		}
		
		setGamesToUI();
	}
	
	private void setGamesToUI() {
		new Thread(new Runnable() {
		    @Override public void run() {
		        Platform.runLater(new Runnable() {
		            @Override public void run() {
		            	playerGames.addAll(new ArrayList<GameData>(gamesMappedByGameID.values()));
		            }
		        });
		    }
		}).start();
	}
	
	public void resetItems() {
		new Thread(new Runnable() {
		    @Override public void run() {
		        Platform.runLater(new Runnable() {
		            @Override public void run() {
		            	playerGames.clear();
		        		friendList.clear();
		            }
		        });
		    }
		}).start();
	}
	
	public void loadSteamGames() {
		resetItems();
		gamesMappedByGameID.clear();
		URL url;
		HttpURLConnection con;
		try {
			url = new URL("http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=" + apiKey + "&steamid=" + getSteamID() + "&include_appinfo=1&include_played_free_games=1&format=json");
			con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json");
			int responsecode = con.getResponseCode(); 

			if(responsecode == 200)
			{
				String str="";
				Scanner sc = new Scanner(url.openStream());
				while(sc.hasNext())
				{
					str+=sc.nextLine();
				}
				sc.close();
				System.out.println(str);
				
				//JSON string to Java Object			
				OwnedGames games = mapper.readValue(str, OwnedGames.class);
				try {
					playerGamesTemp.addAll(games.getGames());
					for(GameData g: playerGamesTemp) {
						gamesMappedByGameID.put(g.getAppid(), g);
					}
				}catch(Exception e) {
					System.out.println("SteamAPI: loading gamelist failed");
				}
				System.out.println("Owned games: " + games.getGame_count());
			}
		} catch (IOException e1) {
			//e1.printStackTrace();
		}
		
		/*
		 * OLD, will be removed later: setting games to UI list - now loading the list after getting selections from db
		new Thread(new Runnable() {
		    @Override public void run() {
		        Platform.runLater(new Runnable() {
		            @Override public void run() {
		            	playerGames.addAll(playerGamesTemp);
		            }
		        });
		    }
		}).start();
		*/
	}
	public void loadSteamFriends() {
		URL url;
		HttpURLConnection con;
		List<Friend> friendIdList = new ArrayList<Friend>();
		try {
			url = new URL("http://api.steampowered.com/ISteamUser/GetFriendList/v0001/?key=" + apiKey + "&steamid=" + getSteamID() + "&relationship=friend");
			con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json");
			int responsecode = con.getResponseCode(); 
			
			if(responsecode == 200)
			{
				String str="";
				Scanner sc = new Scanner(url.openStream());
				while(sc.hasNext())
				{
					str+=sc.nextLine();
				}
				sc.close();
				
				//JSON string to Java Object			
				FriendList response = mapper.readValue(str, FriendList.class);
				friendIdList.addAll(response.getFriends());
				System.out.println("Friends: " + friendList.size());
			}
		} catch (IOException e1) {
			//e1.printStackTrace();
		}
		
		List<String> friendSteamIDList = new ArrayList<>();
		
		for(Friend f:friendIdList) {
			friendSteamIDList.add(f.getSteamid());
		}
		
		//friend list has to be split for requesting profile info (Steam api allows max 100 per request)
		int requiredSplitLists = (int) Math.ceil(friendSteamIDList.size() / 100f);
			
		Map<String, PlayerProfile> profiles = new HashMap<String, PlayerProfile>();
		for(int i = 0; i < requiredSplitLists;i++) {
			List<String> friendSubList; 
			if(i<(requiredSplitLists-1))
				friendSubList = friendSteamIDList.subList(i*100, i*100+100);
			else
				friendSubList = friendSteamIDList.subList(i*100, friendSteamIDList.size());
			profiles.putAll(loadSteamPlayerProfiles(friendSubList));
		}
		
		for(Friend f:friendIdList) {
			PlayerProfile profile = profiles.get(f.getSteamid());
			if(profile!=null) {
				f.setPlayerProfile(profile);
				friendListTemp.add(f);
			}
		}
		new Thread(new Runnable() {
		    @Override public void run() {
		        Platform.runLater(new Runnable() {
		            @Override public void run() {
		            	friendList.addAll(friendListTemp);
		            }
		        });
		    }
		}).start();
	}
	
	private Map<String, PlayerProfile> loadSteamPlayerProfiles(List<String> steamIDList) {
		URL url;
		HttpURLConnection con;
		Map<String, PlayerProfile> profileMap = new HashMap<String, PlayerProfile>();
		try {
			String separatedIDList = String.join(",", steamIDList);
			System.out.println("Friendlist size: " + steamIDList.size());
			String parameters = String.format("key=%s&steamids=%s", apiKey, separatedIDList);
			url = new URL("http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?"+parameters);
			
			System.out.println(url);
			
			con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json");
			int responsecode = con.getResponseCode(); 

			if(responsecode == 200)
			{
				String str="";
				Scanner sc = new Scanner(url.openStream());
				while(sc.hasNext())
				{
					str+=sc.nextLine();
				}
				sc.close();
				System.out.println(str);
				
				//JSON string to Java Object			
				JsonNode rootNode = new ObjectMapper().readTree(new StringReader(str));
				JsonNode innerNode = rootNode.get("response");
				ObjectReader objectReader = mapper.readerFor(new TypeReference<List<PlayerProfile>>() {})
                        .withRootName("players");
				List<PlayerProfile> profiles = objectReader.readValue(innerNode);
				
				for(PlayerProfile p:profiles) {
					profileMap.put(p.getSteamid(), p);
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return profileMap;
	}
	
	public void loadGameSchema(int appID) {
		URL url;
		HttpURLConnection con;
		try {
			String parameters = String.format("key=%s&appid=%s", apiKey, appID);
			url = new URL(" http://api.steampowered.com/ISteamUserStats/GetSchemaForGame/v2/?" + parameters);
			con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json");
			int responsecode = con.getResponseCode(); 

			if(responsecode == 200)
			{
				String str="";
				Scanner sc = new Scanner(url.openStream());
				while(sc.hasNext())
				{
					str+=sc.nextLine();
				}
				sc.close();
				
				//JSON string to Java Object
				System.out.println("schema url: " + url);
				System.out.println("schema response: " + str);
				
				mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false);
				JsonNode rootNode = new ObjectMapper().readTree(new StringReader(str));
				JsonNode innerNode = rootNode.path("game").path("availableGameStats");
				ObjectReader objectReader = mapper.readerFor(new TypeReference<GameStatistics>() {});
				GameStatistics gameStats = objectReader.readValue(innerNode);
				
				gamesMappedByGameID.get(appID).setGameStatistics(gameStats);
				getAchievementCompletionInfo(appID);	
			}

		} catch (IOException e1) {
			//e1.printStackTrace();
		}
	}
	private void getAchievementCompletionInfo(int appID) {
		URL url;
		HttpURLConnection con;
		try {
			String parameters = String.format("key=%s&appid=%s&steamid=%s", apiKey, appID, getSteamID());
			url = new URL(" http://api.steampowered.com/ISteamUserStats/GetPlayerAchievements/v0001/?" + parameters);
			con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json");
			int responsecode = con.getResponseCode(); 

			if(responsecode == 200)
			{
				String str="";
				Scanner sc = new Scanner(url.openStream());
				while(sc.hasNext())
				{
					str+=sc.nextLine();
				}
				sc.close();
				
				//JSON string to Java Object
				mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false);
				
				JsonNode rootNode = new ObjectMapper().readTree(new StringReader(str));
				JsonNode innerNode = rootNode.path("playerstats");
				ObjectReader objectReader = mapper.readerFor(new TypeReference<GameStatistics>() {});
				GameStatistics achievementCompletionInfo = objectReader.readValue(innerNode);
				
				//Mapping achievement schema of the requested game, using name (not display name) as identifier
				Map<String, Achievement> achievements = new HashMap<String, Achievement>();
				for(Achievement ach: gamesMappedByGameID.get(appID).getGameStatistics().getAchievements()) {
					achievements.put(ach.getName(), ach);
				}
				
				//Filling the completion info from this API request to the achievement schema of the game
				for(Achievement a: achievementCompletionInfo.getAchievements()) {
					Achievement achievementSchema = achievements.get(a.getApiname());
					achievementSchema.setAchieved(a.getAchieved());
					achievementSchema.setUnlocktime(a.getUnlocktime());
				}
			}

		} catch (IOException e1) {
			//e1.printStackTrace();
		}
	}
	
	public OwnedGames loadFriendsGames(String friendsID) {

		friendsGames.clear();
		fGamesMappedByGameID.clear();
		URL url;
		HttpURLConnection con;
		
		try {
			url = new URL("http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=" + apiKey + "&steamid=" + friendsID + "&include_appinfo=1&include_played_free_games=1&format=json");
			con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json");
			int responsecode = con.getResponseCode(); 

			if(responsecode == 200)
			{
				String str="";
				Scanner sc = new Scanner(url.openStream());
				while(sc.hasNext())
				{
					str+=sc.nextLine();
				}
				sc.close();
				System.out.println(str);
				
				//JSON string to Java Object			
				OwnedGames games = mapper.readValue(str, OwnedGames.class);
				return games;/*
				try {
					friendsGamesTemp.addAll(games.getGames());
					for(GameData g: friendsGamesTemp) {
						fGamesMappedByGameID.put(g.getAppid(), g);
					}
				}catch(Exception e) {
					System.out.println("SteamAPI: loading gamelist failed");
				}
				System.out.println("Owned games: " + games.getGame_count());*/
			}
		} catch (IOException e1) {
			//e1.printStackTrace();
		}/*
		new Thread(new Runnable() {
		    @Override public void run() {
		        Platform.runLater(new Runnable() {
		            @Override public void run() {
		            	friendsGames.addAll(friendsGamesTemp);
		            }
		        });
		    }
		}).start();*/
		return null;
	}
}
