package com.bookcentric.component.access;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.SessionStatus;

@Controller
public class LoginLogoutController {

	@GetMapping("/login")
	public String login() {
		return ("login");
	}
	
	@GetMapping("/loginFailed")
    public String loginError(Model model) {
        model.addAttribute("loginError", "true");
        return "login";
    }
	
	@GetMapping("/logoutsuccess")
    public String logoutSuccess(SessionStatus session) {
		SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(null);
        session.setComplete();
        return "redirect:/home";
    }
	
	@PostMapping(value = "/postLogin")
    public String postLogin() {
        // read principal out of security context and set it to session
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
               
        return "redirect:/home";
    }
	/*compile('org.thymeleaf.extras:thymeleaf-extras-springsecurity4')*/
}
