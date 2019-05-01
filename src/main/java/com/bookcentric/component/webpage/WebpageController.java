package com.bookcentric.component.webpage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WebpageController {
	
	@Autowired 
	private WebpageService webpageService;

	@GetMapping("/home")
	public ModelAndView viewRegistration(Model model) {
		return webpageService.viewHomepage();		
	}
}
