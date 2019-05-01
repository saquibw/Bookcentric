package com.bookcentric.component.webpage;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service
public class WebpageServiceImpl implements WebpageService {

	@Override
	public ModelAndView viewHomepage() {
		ModelAndView homeView = new ModelAndView("home");
		
		return homeView;
	}

}
