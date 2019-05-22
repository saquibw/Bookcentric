package com.bookcentric.component.user;

import java.util.List;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bookcentric.component.user.deliveryarea.DeliveryArea;
import com.bookcentric.component.user.deliveryarea.DeliveryAreaService;
import com.bookcentric.component.user.parent.Parent;
import com.bookcentric.component.user.paymentmode.PaymentMode;
import com.bookcentric.component.user.paymentmode.PaymentModeService;
import com.bookcentric.component.user.subscription.Subscription;
import com.bookcentric.component.user.subscription.SubscriptionService;
import com.bookcentric.custom.util.Constants;
import com.bookcentric.custom.util.Response;
import com.google.gson.JsonObject;

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
		
		user.setStatus(Constants.STATUS_PENDING);
		userService.add(user);
		
		return "redirect:/user/registration";
	}
	
	@GetMapping("/user/get/{id}")
	public ModelAndView getUser(@PathVariable("id") int id, Model model) {
		ModelAndView editView = new ModelAndView("user-update");
		Optional<User> u = userService.getBy(id);
		User user = u.get();
		
		if(user.getParent() == null) {
			user.setParent(new Parent());
		}

		List<Subscription> subscriptionList = subscriptionService.findAll();
		List<DeliveryArea> deliveryAreaList = deliveryAreaService.findAll();
		List<PaymentMode> paymentModeList = paymentModeService.findAll();
		
		editView.addObject("user", user);
		model.addAttribute("subscriptionList", subscriptionList);
		model.addAttribute("areaList", deliveryAreaList);
		model.addAttribute("paymentModeList", paymentModeList);
		
		return editView;
	}
	
	@PostMapping("/user/update")
	public String updateUser(User user) {
		userService.add(user);
		
		return "redirect:/management/user";
	}
	
	@ResponseBody
	@RequestMapping(value="/user/updatestatus", method=RequestMethod.POST)
	public Response updateStatus(@RequestParam("status") String status, @RequestParam("id") Integer id) {
		Response response = new Response();
		
		if(userService.updateStatus(id, status)) {
			response.setSuccess(true);
		}
		
		return response;
		
	}
}
