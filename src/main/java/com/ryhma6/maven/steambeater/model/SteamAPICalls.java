package com.ryhma6.maven.steambeater.model;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryhma6.maven.steambeater.model.steamAPI.Friend;
import com.ryhma6.maven.steambeater.model.steamAPI.FriendList;
import com.ryhma6.maven.steambeater.model.steamAPI.GameData;
import com.ryhma6.maven.steambeater.model.steamAPI.OwnedGames;
import com.ryhma6.maven.steambeater.model.steamAPI.PlayerProfile;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SteamAPICalls {
	private static ObservableList<GameData> playerGames = FXCollections.observableArrayList();
	private static ObservableList<Friend> friendList = FXCollections.observableArrayList();
	
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
			url = new URL("http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=38FB06680EA5CA6B526B31CBD4E43593&steamid=76561197960434622&include_appinfo=1&format=json");
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
				playerGames = FXCollections.observableArrayList(games.getGames());
				System.out.println("Owned games: " + games.getGame_count());
			}

		} catch (IOException e1) {
			//e1.printStackTrace();
		}
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
				System.out.println(str);
				
				//JSON string to Java Object			
				FriendList response = mapper.readValue(str, FriendList.class);
				friendList = FXCollections.observableArrayList(response.getFriends());
				System.out.println("Friends: " + friendList.size());
			}
		} catch (IOException e1) {
			//e1.printStackTrace();
		}
		for(Friend f:friendList) {
			f.setPlayerProfile(loadSteamPlayerProfile(f.getSteamid()));
		}
	}
	private PlayerProfile loadSteamPlayerProfile(String steamID) {
		URL url;
		HttpURLConnection con;
		PlayerProfile profile = new PlayerProfile();
		try {
			String key = "38FB06680EA5CA6B526B31CBD4E43593";
			String parameters = String.format("key=%s&steamid=%s", key,steamID);
			url = new URL("http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?"+parameters);
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
				profile = mapper.readValue(str, PlayerProfile.class);
			}
		} catch (IOException e1) {
			//e1.printStackTrace();
		}
		return profile;
	}
}
