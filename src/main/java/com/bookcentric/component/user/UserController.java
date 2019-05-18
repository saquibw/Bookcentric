package com.bookcentric.component.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bookcentric.component.user.deliveryarea.DeliveryArea;
import com.bookcentric.component.user.deliveryarea.DeliveryAreaService;
import com.bookcentric.component.user.parent.Parent;
import com.bookcentric.component.user.parent.ParentService;
import com.bookcentric.component.user.paymentmode.PaymentMode;
import com.bookcentric.component.user.paymentmode.PaymentModeService;
import com.bookcentric.component.user.subscription.Subscription;
import com.bookcentric.component.user.subscription.SubscriptionService;

@Controller
public class UserController {
	
	@Autowired private UserService userService;
	
	@Autowired private SubscriptionService subscriptionService;
	
	@Autowired private DeliveryAreaService deliveryAreaService;
	
	@Autowired private PaymentModeService paymentModeService;
	
	
	
	@GetMapping("/user/registration")
	public ModelAndView viewRegistration(Model model) {
		ModelAndView regView = new ModelAndView("user-registration");
		
		List<Subscription> subscriptionList = subscriptionService.findAll();
		List<DeliveryArea> deliveryAreaList = deliveryAreaService.findAll();
		List<PaymentMode> paymentModeList = paymentModeService.findAll();
		
		model.addAttribute("pageTitle", "Registration page");
		model.addAttribute("user", new User());
		model.addAttribute("subscriptionList", subscriptionList);
		model.addAttribute("areaList", deliveryAreaList);
		model.addAttribute("paymentModeList", paymentModeList);
		
		return regView;	
	}
	
	@PostMapping(value="/user/add")
	public String addUser(User user) {
		if(!user.getParent().getFirstName().equals("")) {
			
			Parent parent = new Parent();
			parent.setFirstName(user.getParent().getFirstName());
			parent.setLastName(user.getParent().getLastName());
			parent.setOccupation(user.getParent().getOccupation());
			parent.setPhone(user.getParent().getPhone());
			parent.setRelation(user.getParent().getRelation());
			
			user.setParent(parent);
		} else {
			user.setParent(null);
		}
		userService.add(user);
		
		return "redirect:/user/registration"; 
	}
}
