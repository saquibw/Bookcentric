package com.bookcentric.component.user.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bookcentric.component.user.User;
import com.bookcentric.component.user.security.UserSecurityService;

@Controller
public class UserDashboardController {
	
	@Autowired UserSecurityService userSecurityService;
	
	@GetMapping("/user/dashboard")
	public ModelAndView viewDashboard() {
		
		ModelAndView dashBoardView = new ModelAndView("user-dashboard");
		
		User user = userSecurityService.getLoggedInUser();;
		
		dashBoardView.addObject("pageTitle", "Member Dashboard");
		dashBoardView.addObject("user", user);
		
		return dashBoardView;
	}
}
