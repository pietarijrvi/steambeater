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
import com.ryhma6.maven.steambeater.model.steamAPI.GameData;
import com.ryhma6.maven.steambeater.model.steamAPI.OwnedGames;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SteamAPICalls {
	private static ObservableList<GameData> playerGames = FXCollections.observableArrayList();
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
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
