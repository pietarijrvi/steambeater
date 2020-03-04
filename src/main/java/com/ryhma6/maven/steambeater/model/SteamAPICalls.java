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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SteamAPICalls {
	private static ObservableList<GameData> playerGames = FXCollections.observableArrayList();
	private static ObservableList<Friend> friendList = FXCollections.observableArrayList();
	private Map<Integer,GameData> gamesMappedByGameID = new HashMap<Integer,GameData>();
	
	private ObjectMapper mapper = new ObjectMapper();
	
	public SteamAPICalls() {
		mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);

		GameData g = new GameData();
		g.setName("test");
		playerGames.add(g);
	}
	
	public static ObservableList<GameData> getOwnedGames() {
		return playerGames;
	}
	
	public static ObservableList<Friend> getFriendList() {
		return friendList;
	}
	
	public void init() {
		URL url;
		HttpURLConnection con;
		try {
			url = new URL("http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=38FB06680EA5CA6B526B31CBD4E43593&steamid=76561197960434622&include_appinfo=1&include_played_free_games=1&format=json");
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
					playerGames = FXCollections.observableArrayList(games.getGames());
					for(GameData g: playerGames) {
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
		getGameSchema(218620);
	}
	public void loadSteamFriends() {
		URL url;
		HttpURLConnection con;
		try {
			url = new URL("http://api.steampowered.com/ISteamUser/GetFriendList/v0001/?key=38FB06680EA5CA6B526B31CBD4E43593&steamid=76561197960435530&relationship=friend");
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
				friendList = FXCollections.observableArrayList(response.getFriends());
				System.out.println("Friends: " + friendList.size());
			}
		} catch (IOException e1) {
			//e1.printStackTrace();
		}
		List<String> friendSteamIDList = new ArrayList<>();
		for(Friend f:friendList) {
			//temporary limit (api allows max 100 per request)
			if(friendSteamIDList.size()<100)
				friendSteamIDList.add(f.getSteamid());
		}
		Map<String, PlayerProfile> profiles = loadSteamPlayerProfiles(friendSteamIDList);
		
		for(Friend f:friendList) {
			PlayerProfile profile = profiles.get(f.getSteamid());
			if(profile!=null)
				f.setPlayerProfile(profile);
		}
	}
	private Map<String, PlayerProfile> loadSteamPlayerProfiles(List<String> steamIDList) {
		URL url;
		HttpURLConnection con;
		Map<String, PlayerProfile> profileMap = new HashMap<String, PlayerProfile>();
		try {
			String apiKey = "38FB06680EA5CA6B526B31CBD4E43593";
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
				
				//PlayerProfile[] profiles = mapper.readValue(str, PlayerProfile[].class);
				
				
				for(PlayerProfile p:profiles) {
					profileMap.put(p.getSteamid(), p);
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return profileMap;
	}
	
	public void getGameSchema(int appID) {
		URL url;
		HttpURLConnection con;
		try {
			String apiKey = "38FB06680EA5CA6B526B31CBD4E43593";
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
				
				GameStatistics gameStats = mapper.readValue(str, GameStatistics.class);
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
			String apiKey = "38FB06680EA5CA6B526B31CBD4E43593";
			String parameters = String.format("key=%s&appid=%s", apiKey, appID);
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
				GameStatistics achievementCompletionInfo = mapper.readValue(str, GameStatistics.class);
				
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
}
