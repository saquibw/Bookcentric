package com.bookcentric.component.utils;

import javax.mail.MessagingException;

public interface EmailService {
	
	public void sendSimpleEmail(String to, String subject, String message);
	
	public void sendHtmlEmail(String to, String subject, String message) throws MessagingException;
}
