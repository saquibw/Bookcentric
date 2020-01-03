package com.bookcentric.component.giftsubscription;

import javax.mail.MessagingException;

public interface GiftSubscriptionService {
	public void sendGiftSubscriptionEmail(GiftSubscription giftSubscription) throws MessagingException;
}
