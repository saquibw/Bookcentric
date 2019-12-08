package com.bookcentric.component.wishlist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.bookcentric.component.utils.EmailService;
import com.bookcentric.config.AppConfig;

@Service
public class WishlistServiceImpl implements WishlistService {
	
@Autowired EmailService emailService;
	
	@Autowired AppConfig config;

	@Async
	@Override
	public void sendWishlistEmail(Wishlist wishlist) {
		String to = config.getEmailRecipient();
		String subject = "Wish for a new book";
		
		StringBuilder text = new StringBuilder();
		text.append(String.format("User %s %s (%s) has requested for following books:", wishlist.getFirstName(), wishlist.getLastName(), wishlist.getMembershipNo()));
		text.append("\n\n");
		text.append(String.format("%s", wishlist.getRequests()));
		
		emailService.sendSimpleEmail(to, subject, text.toString());		
	}

}
