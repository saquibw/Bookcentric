package com.bookcentric.component.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/user/registration")
	public ModelAndView viewRegistration(Model model) {
		return userService.viewRegistration(model);		
	}
	
	@PostMapping(value="/user/add")
	public String addUser(User user) {
		userService.add(user);
		
		return "redirect:/user/registration"; 
	}
}
