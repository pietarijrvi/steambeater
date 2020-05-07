package com.ryhma6.maven.steambeater.model;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Converts Steam API's epoch timestamp to readable time
 *
 */
public class TimeConverter {
	/**
	 * Converts epoch timestamp got from the Steam API to local time
	 * @param millis epoch milliseconds to be converted
	 * @return Timestamp String 
	 */
	public static String epochMillisToLocalTimestamp(long millis) {
		Instant instant = Instant.ofEpochMilli(millis);
		ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, ZoneOffset.systemDefault());

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(LanguageProvider.getString("dateTimeFormat"));
		String output = formatter.format(zdt);
		return output;
	}
}
