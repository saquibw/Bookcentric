package com.bookcentric.component.user.planduration;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PlanDurationController {

	@GetMapping("/planDuration/view")
	public ModelAndView view() {
		ModelAndView model = new ModelAndView("plan-duration");
		model.addObject("planDuration", new PlanDuration());
		
		return model;
	}
	
	 
}
