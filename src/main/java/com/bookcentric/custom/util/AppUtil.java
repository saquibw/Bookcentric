package com.bookcentric.custom.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AppUtil {
	
	public static String updateLocalDateFormat(String date, String format) {
		DateTimeFormatter defaultFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter desiredFormat = DateTimeFormatter.ofPattern(format);
		
		LocalDate customDate = LocalDate.parse(date, defaultFormat);

		return customDate.format(desiredFormat);
	}
}
