package com.bookcentric.component.giftsubscription;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookcentric.component.email.Email;
import com.bookcentric.component.email.EmailService;
import com.bookcentric.config.AppConfig;

@Service
public class GiftSubscriptionServiceImpl implements GiftSubscriptionService {
	
	@Autowired EmailService emailService;	
	@Autowired AppConfig config;

	@Override
	public void sendGiftSubscriptionEmail(GiftSubscription giftSubscription) throws MessagingException {
		String to = config.getEmailRecipient();
		String subject = "Request to gift a subscription";
		
		StringBuilder text = new StringBuilder();
		text.append("Someone has requested to gift a subscription. The details are as follows:");
		text.append("<br><br>");
		
		text.append("<table border='1'>");
		text.append(String.format("<tr><td>Name of giver:</td><td> %s </td></tr>", giftSubscription.getGiver()));
		text.append(String.format("<tr><td>Name of recipient:</td><td> %s </td></tr>", giftSubscription.getRecipient()));
		text.append(String.format("<tr><td>Email address:</td><td> %s </td></tr>", giftSubscription.getEmail()));
		text.append(String.format("<tr><td>Phone number:</td><td> %s </td></tr>", giftSubscription.getPhone()));
		text.append(String.format("<tr><td>Gift amount (BDT):</td><td> %.2f </td></tr>", giftSubscription.getAmount()));
		text.append(String.format("<tr><td>Delivery address:</td><td> %s </td></tr>", giftSubscription.getAddress()));
		text.append(String.format("<tr><td>Date of delivery:</td><td> %s </td></tr>", getFullDate(giftSubscription.getDeliveryDate())));
		text.append(String.format("<tr><td>Certificate option chosen:</td><td> %d </td></tr>", giftSubscription.getCertificateDesign()));
		text.append(String.format("<tr><td>Message:</td><td> %s </td></tr>", getOptionMessage(giftSubscription)));
		
		text.append("</table>");
		
		String message = text.toString();
		Email email = new Email(to, subject, message);
		
		emailService.sendHtmlEmail(email);
	}
	
	private String getFullDate(String date) {
		LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		
		StringBuilder d = new StringBuilder();
		d.append(String.format("%d %s %s", localDate.getDayOfMonth(), localDate.getMonth(), localDate.getYear()));
		
		return d.toString(); 
	}
	
	private String getOptionMessage(GiftSubscription giftSubscription) {
		StringBuilder text = new StringBuilder();
		
		if(giftSubscription.getOption() == 1) {
			text.append("I'd like to fill in the gift card myself");
		} else if(giftSubscription.getOption() == 2) {
			text.append("Please write a message for me:");
			text.append("\n\n");
			text.append(giftSubscription.getMessage());
		}
		
		return text.toString();
	}

}
