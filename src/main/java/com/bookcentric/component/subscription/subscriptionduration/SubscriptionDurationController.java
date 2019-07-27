package com.bookcentric.component.subscription.subscriptionduration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SubscriptionDurationController {
	
	@Autowired SubscriptionDurationService subscriptionDurationService;

	@GetMapping({"/subscriptionduration/view", "/subscriptionduration/edit/{id}"})
	public ModelAndView view(@PathVariable(name="id", required=false) Integer id) {
		ModelAndView view = new ModelAndView("subscription-duration");
		
		SubscriptionDuration subscriptionDuration = new SubscriptionDuration();
		
		if(id != null && id > 0) {
			subscriptionDuration = subscriptionDurationService.findBy(id);
		}
		List<SubscriptionDuration> subscriptionDurationList = subscriptionDurationService.findAll();
		
		view.addObject("subscriptionDuration", subscriptionDuration);
		view.addObject("subscriptionDurationList", subscriptionDurationList);
		view.addObject("pageTitle", "BookCentric - subscription Duration");
		
		return view;
	}
	
	@PostMapping("/subscriptionduration/add")
	public String saveSubscriptionDuration(SubscriptionDuration subscriptionDuration) {
		subscriptionDurationService.save(subscriptionDuration);
		
		return "redirect:/subscriptionduration/view";
	}
	
	@GetMapping("/subscriptionduration/delete/{id}")
	public String deleteSubscriptionDuration(@PathVariable(name="id") Integer id) {
		SubscriptionDuration subscriptionDuration = subscriptionDurationService.findBy(id);
		subscriptionDurationService.delete(subscriptionDuration);
		
		return "redirect:/subscriptionduration/view";
	}
}
