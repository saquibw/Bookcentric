package com.bookcentric.component.access;

import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
        model.addAttribute("error", "true");
        return "login";
    }
	
	@GetMapping("/logout")
    public String logout(SessionStatus session) {
        SecurityContextHolder.getContext().setAuthentication(null);
        session.setComplete();
        return "redirect:/home";
    }
	
	@PostMapping(value = "/postLogin")
    public String postLogin() {
        

        // read principal out of security context and set it to session
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.toString());
        System.out.println(SecurityContextHolder.getContext().getAuthentication().toString());
        
        return "redirect:/home";
    }

}
