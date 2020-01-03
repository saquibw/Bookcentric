package com.bookcentric.component.giftsubscription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GiftSubscriptionController {
	
	@Autowired GiftSubscriptionService giftSubscriptionService;
	
	@GetMapping("/gift-subscription")
	public ModelAndView viewGiftSubscription() {
		ModelAndView mv = new ModelAndView("gift-subscription");
		
		mv.addObject("pageTitle", "BookCentric - Gift a subscription");
		mv.addObject("giftSubscription", new GiftSubscription());
		
		return mv;
	}
	
	@PostMapping("/gift-subscription")
	public ModelAndView submitGiftSubscription(GiftSubscription giftSubscription) {
		ModelAndView mv = new ModelAndView("gift-subscription");
		
		try {
			giftSubscriptionService.sendGiftSubscriptionEmail(giftSubscription);
			mv.addObject("subscriptionSuccess", true);
		} catch (Exception e) {
			mv.addObject("subscriptionFailure", true);
			e.printStackTrace();
		}
				
		mv.addObject("giftSubscription", new GiftSubscription());
		
		return mv;
	}
}
