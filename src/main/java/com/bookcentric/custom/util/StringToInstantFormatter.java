package com.bookcentric.custom.util;

import java.text.ParseException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class StringToInstantFormatter /*implements Formatter<Instant>*/ {

	/*@Override
	public Instant convert(String source) {
		
		return Instant.parse(source);
	}*/

	/*@Override
	public String print(Instant object, Locale locale) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Instant parse(String text, Locale locale) throws ParseException {
		System.out.println(text);
		System.out.println("hello");
		return Instant.parse(text);
	}*/

}
