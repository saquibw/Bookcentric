package com.bookcentric.component.user;

import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.bookcentric.component.user.subscription.Subscription;

public interface UserService {
	public void add(User user);
}
