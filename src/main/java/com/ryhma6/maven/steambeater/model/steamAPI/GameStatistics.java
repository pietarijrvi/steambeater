package com.ryhma6.maven.steambeater.model.steamAPI;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class GameStatistics {
	@Getter @Setter private String steamID;
	@Getter @Setter private String gameName;
	@Getter @Setter private List<Achievement> achievements;
	@Getter @Setter private List<Stat> stats;
}
