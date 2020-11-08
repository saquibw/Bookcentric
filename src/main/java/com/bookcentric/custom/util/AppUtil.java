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
	
	public static String getEmailSignature() {
		StringBuilder text = new StringBuilder();
		
		text.append("Thank you,");
		text.append("<br><br>");
		text.append("Team Bookcentric.");
		text.append("<br><br>");
		text.append("<img src='http://bookcentricbd.com/images/bc-logo.png' alt='logo' width='100' height='100'></img>");
		text.append("<br><br>");
		text.append("WhatsApp : +8801731374781\n");
		text.append("<br>");
		text.append("Email : bookcentricbd@gmail.com\n");
		text.append("<br>");
		text.append("Facebook: https://facebook.com/bookcentric");
		text.append("<br>");
		text.append("Instagram : https://instagram.com/bookcentricbd");
		text.append("<br>");
		
		return text.toString();
	}
}
