package com.bookcentric.component.user.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookcentric.component.user.User;
import com.bookcentric.component.user.UserService;

@Service
public class UserSecurityServiceImpl implements UserSecurityService{
	
	@Autowired private UserService userService;

	@Override
	public User getLoggedInUser() {

		return userService.getBy(16);
	}

}
