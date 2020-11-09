package com.bookcentric.component.email;

import java.io.File;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Email {
	
	public Email(String to, String subject, String message) {
		this.setTo(to);
		this.setSubject(subject);
		this.setMessage(message);
	}
	
	private String to;
	private String[] cc;
	private String subject;
	private String message;
	private Map<String, File> attachments;
	
}
