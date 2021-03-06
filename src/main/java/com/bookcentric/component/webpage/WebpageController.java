package com.bookcentric.component.webpage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WebpageController {
	
	@Autowired 
	private WebpageService webpageService;
	
	@GetMapping("/")
	public String view() {
		return "redirect:/home";		
	}

	@GetMapping("/home")
	public ModelAndView viewHome() {
		return webpageService.viewHomepage();		
	}
	
	@GetMapping("/donate-books")
	public String viewDonateBookPage() {
		return "donate-book";
	}
	
	@GetMapping("/coming-soon")
	public String comingSoon() {
		return "coming-soon";
	}
	
	@GetMapping("/faq")
	public ModelAndView getFaq() {
		return webpageService.viewFaqPage();
	}
}
