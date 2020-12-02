package com.bookcentric.component.user.passwordreset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.bookcentric.component.user.User;
import com.bookcentric.component.user.UserService;
import com.bookcentric.component.user.security.UserSecurityService;
import com.bookcentric.component.utils.UtilService;

@Controller
public class PasswordResetController {
	@Autowired UserSecurityService securityService;
	@Autowired UserService userService;
	@Autowired UtilService utilService;

	@GetMapping("/password/reset")
	public ModelAndView viewPasswordResetPage() {
		PasswordReset pw = new PasswordReset();
		ModelAndView mv = new ModelAndView("password-reset");
		
		User user = securityService.getLoggedInUser();
		
		if(user != null) {
			pw.setUserId(user.getId());
		}
		mv.addObject("passwordReset", pw);
		mv.addObject("pageTitle", "BookCentric - Reset Password");
		
		return mv;
	}
	
	@PostMapping("/password/reset")
	public String updatePassword(PasswordReset pw, Model model, SessionStatus session) {
		String page = "login";

		if(pw != null) {
			User user = securityService.getLoggedInUser();
			
			if(!utilService.matchPassword(pw.getCurrentPassword(), user.getPassword())) {
				model.addAttribute("wrongPassword", "true");
				page = "password-reset";
			} else {
				String encodedPw = utilService.encryptPassword(pw.getNewPassword());
				user.setPassword(encodedPw);
				userService.add(user);
				
				SecurityContext context = SecurityContextHolder.getContext();
		        context.setAuthentication(null);
		        session.setComplete();
		        
		        model.addAttribute("passwordResetSuccess", "true");		
			}
		}
		return page;
	}
}
