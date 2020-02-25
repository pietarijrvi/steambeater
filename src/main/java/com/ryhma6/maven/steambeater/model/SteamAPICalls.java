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
import com.ryhma6.maven.steambeater.model.steamAPI.OwnedGames;

public class SteamAPICalls {
	private ObjectMapper mapper = new ObjectMapper();
	
	public SteamAPICalls() {
		mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
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
				System.out.println("\nJSON data in string format");
				System.out.println(str);
				sc.close();
				
				//JSON string to Java Object			
				OwnedGames ownedGames = mapper.readValue(str, OwnedGames.class);
				System.out.println(ownedGames.getGame_count());
			}

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
