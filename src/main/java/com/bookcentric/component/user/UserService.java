package com.bookcentric.component.user;

import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

public interface UserService {
	public ModelAndView viewRegistration(Model model);
	public void add(User user);
}
