package com.bookcentric.component.management;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	public ModelAndView viewUserManagement(Model model, @RequestParam(required = false) String status) {
		ModelAndView userView = new ModelAndView("user-management");
		
		List<User> userList = userService.getAll().stream()
				.filter(user -> !user.getRole().equals(Constants.ROLE_ADMIN))
				.sorted(Comparator.comparing(User::getCreatedAt).reversed())
				.collect(Collectors.toList());
		
		if (status != null) {
			userList = userList.stream()
					.filter(user -> user.getStatus().getName().equals(status))
					.collect(Collectors.toList());
		}
		
		model.addAttribute("pageTitle", "BookCentric - User Management");
		model.addAttribute("userList", userList);
		
		return userView;
	}
	
	@GetMapping("/management/user/history")
	public ModelAndView viewUserHistory(Model model) {
		ModelAndView userView = new ModelAndView("user-history");
		
		List<User> activeUserList = userService.getAll().stream()
				.filter(user -> Constants.STATUS_ACTIVE.equals(user.getStatus().getName()) && !user.getRole().equals(Constants.ROLE_ADMIN))
				.sorted(Comparator.comparing(User::getFirstName))
				.collect(Collectors.toList());
		
		userView.addObject("pageTitle", "BookCentric - User History");
		userView.addObject("userList", activeUserList);
		
		return userView;
	}

}
