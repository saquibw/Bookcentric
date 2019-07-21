package com.bookcentric.component.utils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
	
	@Autowired JavaMailSender emailSender;

	@Override
	public void sendSimpleEmail(String to, String subject, String message) {
		SimpleMailMessage m = new SimpleMailMessage();
		m.setTo(to);
		m.setSubject(subject);
		m.setText(message);

		emailSender.send(m);
	}

	@Override
	public void sendHtmlEmail(String to, String subject, String message) throws MessagingException {
		MimeMessage msg = emailSender.createMimeMessage();
		
		MimeMessageHelper helper = new MimeMessageHelper(msg);
		
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(message, true);
		
		emailSender.send(msg);
	}

}
