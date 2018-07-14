package com.bookcentric.component.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/user/add", method=RequestMethod.POST)
	public void addUser(@RequestBody User user) {

		System.out.println(user.getFirstName());
		System.out.println(user.getLastName());
		userService.add(user);
	}
}
