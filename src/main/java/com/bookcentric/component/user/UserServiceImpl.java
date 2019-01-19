package com.bookcentric.component.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.bookcentric.component.user.subscription.Subscription;
import com.bookcentric.component.user.subscription.SubscriptionRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private SubscriptionRepository subscriptionRepository;
	
	@Override
	public ModelAndView viewRegistration(Model model) {
		ModelAndView regView = new ModelAndView("user-registration");
		
		List<Subscription> subscriptionList = subscriptionRepository.findAll();
		
		model.addAttribute("pageTitle", "Registration page");
		model.addAttribute("user", new User());
		model.addAttribute("subscriptionList", subscriptionList);
		
		return regView;
	}
	
	public void add(User user) {
		
		userRepository.save(user);
		
	}
}
