package com.bookcentric.component.utils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
	
	@Autowired JavaMailSender emailSender;

	@Async
	@Override
	public void sendSimpleEmail(String to, String subject, String message) {
		log.debug("Sending email with subject {} to {}", subject, to);
		
		SimpleMailMessage m = new SimpleMailMessage();
		m.setTo(to);
		m.setSubject(subject);
		m.setText(message);

		emailSender.send(m);
		
		log.debug("Email sending success with subject {} to {}", subject, to);
	}

	@Async
	@Override
	public void sendHtmlEmail(String to, String subject, String message) throws MessagingException {
		log.debug("Sending email with subject {} to {}", subject, to);
		
		MimeMessage msg = emailSender.createMimeMessage();
		
		MimeMessageHelper helper = new MimeMessageHelper(msg);
		
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(message, true);
		
		emailSender.send(msg);
		
		log.debug("Email sending success with subject {} to {}", subject, to);
	}

}
