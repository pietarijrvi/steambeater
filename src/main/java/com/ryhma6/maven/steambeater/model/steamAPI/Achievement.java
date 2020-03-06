package com.ryhma6.maven.steambeater.model.steamAPI;

import lombok.Getter;
import lombok.Setter;

public class Achievement {
	@Getter @Setter private String apiname;
	@Getter @Setter private String achieved;
	@Getter @Setter private int unlocktime;
	@Getter @Setter private String name;
	@Getter @Setter private int defaultvalue;
	@Getter @Setter private int displayName;
	@Getter @Setter private String description;
	@Getter @Setter private String icon;
	@Getter @Setter private String icongray;
}
