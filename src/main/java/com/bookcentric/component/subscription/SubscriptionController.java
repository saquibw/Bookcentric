package com.bookcentric.component.subscription;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bookcentric.component.subscription.category.Category;
import com.bookcentric.component.subscription.category.CategoryService;
import com.bookcentric.component.subscription.subscriptionduration.SubscriptionDuration;
import com.bookcentric.component.subscription.subscriptionduration.SubscriptionDurationService;

@Controller
public class SubscriptionController {
	
	@Autowired SubscriptionService subscriptionService;
	@Autowired CategoryService categoryService;
	@Autowired SubscriptionDurationService subscriptionDurationService;
	
	@GetMapping({"/subscription/view", "/subscription/edit/{id}"})
	public ModelAndView view(@PathVariable(name="id", required=false) Integer id) {
		ModelAndView view = new ModelAndView("subscription");
		
		Subscription subscription = new Subscription();
		if(id != null && id > 0) {
			subscription = subscriptionService.findBy(id);
		}
		
		
		List<Subscription> subscriptionList = subscriptionService.findAll();
		List<Category> categoryList = categoryService.findAll();
		List<SubscriptionDuration> subscriptionDurationList = subscriptionDurationService.findAll();
		
		view.addObject("subscription", subscription);
		view.addObject("subscriptionList", subscriptionList);
		view.addObject("categoryList", categoryList);
		view.addObject("subscriptionDurationList", subscriptionDurationList);
		view.addObject("pageTitle", "BookCentric - Subscription");
		
		return view;
	}
	
	@Transactional
	@PostMapping("/subscription/add")
	public String saveSubscription(Subscription subscription) {		
		subscriptionService.save(subscription);
		
		return "redirect:/subscription/view";
	}
	
	@GetMapping("/subscription/delete/{id}")
	public String deleteSubscription(@PathVariable(name="id") Integer id) {
		Subscription subscription = subscriptionService.findBy(id);
		subscriptionService.delete(subscription);
		
		return "redirect:/subscription/view";
	}
	
	@GetMapping("/subscription/view/plans")
	public ModelAndView viewSubscriptionPlans() {
		ModelAndView view = new ModelAndView("subscription-plans");
		
		List<Subscription> subscriptionList = subscriptionService.findAll();
		List<Category> categoryList = categoryService.findAll();
		
		List<SubscriptionResponse> plans = subscriptionService.getSubscriptionPlansData(subscriptionList, categoryList);
		List<SubscriptionResponse> childrenPlans = subscriptionService.getSubscriptionChildrenPlansData(subscriptionList, categoryList);
		List<SubscriptionResponse> familyPlans = subscriptionService.getSubscriptionFamilyPlansData(subscriptionList, categoryList);
		
		view.addObject("plans", plans);
		view.addObject("childrenPlans", childrenPlans);
		view.addObject("familyPlans", familyPlans);
		view.addObject("pageTitle", "BookCentric - Subscription Plans");
		
		return view;
	}
	
	@GetMapping("/subscription/view/deliveryinformation")
	public ModelAndView viewDeliveryInformation() {
		ModelAndView view = new ModelAndView("delivery-information");
		
		return view;
	}
}
