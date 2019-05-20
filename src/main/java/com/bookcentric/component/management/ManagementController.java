package com.bookcentric.component.management;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bookcentric.component.user.User;
import com.bookcentric.component.user.UserService;
import com.bookcentric.custom.util.Constants;

@Controller
public class ManagementController {
	
	@Autowired UserService userService;
	
	@GetMapping("/management")
	public ModelAndView viewManagement(Model model) {
		ModelAndView managementView = new ModelAndView("management-home");
		
		model.addAttribute("pageTitle", "BookCentric - Management");
		
		return managementView;
	}
	
	@GetMapping("/management/user")
	public ModelAndView viewUserManagement(Model model) {
		ModelAndView userView = new ModelAndView("user-management");
		
		List<User> userList = userService.getAll();
		
		model.addAttribute("pageTitle", "BookCentric - User management");
		model.addAttribute("userList", userList);
		
		return userView;
	}
	
	@GetMapping("/management/user/history")
	public ModelAndView viewUserHistory(Model model) {
		ModelAndView userView = new ModelAndView("user-history");
		
		List<User> userList = userService.getAll();
		
		List<User> activeUserList = userList.stream().filter(c -> Constants.STATUS_ACTIVE.equals(c.getStatus())).collect(Collectors.toList());
		
		userView.addObject("pageTitle", "BookCentric - User history");
		userView.addObject("userList", activeUserList);
		
		return userView;
	}

}
