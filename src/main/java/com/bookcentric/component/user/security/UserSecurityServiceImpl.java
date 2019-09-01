package com.bookcentric.component.user.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.bookcentric.component.user.User;
import com.bookcentric.component.user.UserService;

@Service
public class UserSecurityServiceImpl implements UserSecurityService{
	
	@Autowired private UserService userService;

	@Override
	public User getLoggedInUser() {     
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User user = new User();
		if(authentication != null) {
			String email = authentication.getName();
			user = userService.getByEmail(email);
		}
		
		return user;
	}

}
