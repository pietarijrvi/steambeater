package com.ryhma6.maven.steambeater.model.steamAPI;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class GameData {
	@Getter @Setter private int appid;
	@Getter @Setter private String name;
	private String img_icon_url;
	private String img_logo_url;
	@Getter @Setter private String has_community_visible_stats;
	@Getter @Setter private int playtime_forever;
	@Getter @Setter private int playtime_windows_forever;
	@Getter @Setter private int playtime_mac_forever;
	@Getter @Setter private int playtime_linux_forever;
	@Getter @Setter private int playtime_2weeks;
	@Getter @Setter private GameStatistics gameStatistics;
	
	@Getter @Setter private boolean unbeatable = false;
	@Getter @Setter private boolean ignored = false;
	@Getter @Setter private boolean beaten = false;
	
	public GameData() {
	}
	public GameData(int appid, String name, int playtime_forever) {
		this.appid=appid;
		this.name=name;
		this.playtime_forever=playtime_forever;
	}
	
	public String getImg_icon_url() {
		String imgUrl = null;
		if(img_logo_url!=null)
			imgUrl = String.format("http://media.steampowered.com/steamcommunity/public/images/apps/%s/%s.jpg", appid, img_icon_url);
		return imgUrl;
	}
	
	public void setImg_icon_url(String img_icon_url) {
		this.img_icon_url = img_icon_url;
	}
	
	public String getImg_logo_url() {
		String imgUrl = null;
		if(img_logo_url!=null)
			imgUrl = String.format("http://media.steampowered.com/steamcommunity/public/images/apps/%s/%s.jpg", appid, img_logo_url);
		return imgUrl;	
	}
	
	public void setImg_logo_url(String img_logo_url) {
		this.img_logo_url = img_logo_url;
	}
}
