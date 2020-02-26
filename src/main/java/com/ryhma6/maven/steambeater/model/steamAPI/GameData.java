package com.ryhma6.maven.steambeater.model.steamAPI;

public class GameData {
	private int appid;
	private String name;
	private String img_icon_url;
	private String img_logo_url;
	private String has_community_visible_stats;
	private int playtime_forever;
	private int playtime_windows_forever;
	private int playtime_mac_forever;
	private int playtime_linux_forever;
	private int playtime_2weeks;
	
	private boolean unbeatable = false;
	private boolean ignored = false;
	private boolean beaten = false;
	
	
	public int getAppid() {
		return appid;
	}
	public void setAppid(int appid) {
		this.appid = appid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImg_icon_url() {
		return img_icon_url;
	}
	public void setImg_icon_url(String img_icon_url) {
		this.img_icon_url = img_icon_url;
	}
	public String getImg_logo_url() {
		return img_logo_url;
	}
	public void setImg_logo_url(String img_logo_url) {
		this.img_logo_url = img_logo_url;
	}
	public String getHas_community_visible_stats() {
		return has_community_visible_stats;
	}
	public void setHas_community_visible_stats(String has_community_visible_stats) {
		this.has_community_visible_stats = has_community_visible_stats;
	}
	public int getPlaytime_forever() {
		return playtime_forever;
	}
	public void setPlaytime_forever(int playtime_forever) {
		this.playtime_forever = playtime_forever;
	}
	public int getPlaytime_windows_forever() {
		return playtime_windows_forever;
	}
	public void setPlaytime_windows_forever(int playtime__windows_forever) {
		this.playtime_windows_forever = playtime__windows_forever;
	}
	public int getPlaytime_mac_forever() {
		return playtime_mac_forever;
	}
	public void setPlaytime_mac_forever(int playtime_mac_forever) {
		this.playtime_mac_forever = playtime_mac_forever;
	}
	public int getPlaytime_linux_forever() {
		return playtime_linux_forever;
	}
	public void setPlaytime_linux_forever(int playtime_linux_forever) {
		this.playtime_linux_forever = playtime_linux_forever;
	}
	public int getPlaytime_2weeks() {
		return playtime_2weeks;
	}
	public void setPlaytime_2weeks(int playtime_2weeks) {
		this.playtime_2weeks = playtime_2weeks;
	}
	public boolean isUnbeatable() {
		return unbeatable;
	}
	public void setUnbeatable(boolean unbeatable) {
		this.unbeatable = unbeatable;
	}
	public boolean isIgnored() {
		return ignored;
	}
	public void setIgnored(boolean ignored) {
		this.ignored = ignored;
	}
	public boolean isBeaten() {
		return beaten;
	}
	public void setBeaten(boolean beaten) {
		this.beaten = beaten;
	}
	
}
