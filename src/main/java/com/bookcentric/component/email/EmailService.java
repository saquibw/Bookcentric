package com.bookcentric.component.email;

import javax.mail.MessagingException;

public interface EmailService {
	
	public void sendSimpleEmail(String to, String subject, String message);
	
	public void sendHtmlEmail(Email email) throws MessagingException;
}
