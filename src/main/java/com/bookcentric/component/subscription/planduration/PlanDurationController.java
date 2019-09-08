package com.bookcentric.component.subscription.planduration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PlanDurationController {
	
	@Autowired PlanDurationService planDurationService;

	//@GetMapping({"/planduration/view", "/planduration/edit/{id}"})
	public ModelAndView view(@PathVariable(name="id", required=false) Integer id) {
		ModelAndView view = new ModelAndView("plan-duration");
		
		PlanDuration planDuration = new PlanDuration();
		
		if(id != null && id > 0) {
			planDuration = planDurationService.findBy(id);
		}
		List<PlanDuration> planDurationList = planDurationService.findAll();
		
		view.addObject("planDuration", planDuration);
		view.addObject("planDurationList", planDurationList);
		view.addObject("pageTitle", "BookCentric - Plan Duration");
		
		return view;
	}
	
	//@PostMapping("/planduration/add")
	public String savePlanDuration(PlanDuration planDuration) {
		planDurationService.save(planDuration);
		
		return "redirect:/planduration/view";
	}
	
	//@GetMapping("/planduration/delete/{id}")
	public String deletePlanDuration(@PathVariable(name="id") Integer id) {
		PlanDuration planDuration = planDurationService.findBy(id);
		planDurationService.delete(planDuration);
		
		return "redirect:/planduration/view";
	}	 
}
