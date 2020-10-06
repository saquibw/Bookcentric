package com.bookcentric.component.user.testimonial;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bookcentric.custom.util.Response;

@Controller
public class TestimonialController {

	@Autowired TestimonialService testimonialService;
	
	@GetMapping({"/testimonials/view", "/testimonials/edit/{id}"})
	public ModelAndView viewTestimonialPage(@PathVariable(name="id", required=false) Integer id) {
		ModelAndView view = new ModelAndView("testimonial");
		
		Testimonial testimonial = new Testimonial();
		
		if(id != null && id > 0) {
			testimonial = testimonialService.findBy(id);
		}
		List<Testimonial> testimonialList = testimonialService.findAll();
		
		view.addObject("testimonial", testimonial);
		view.addObject("testimonialList", testimonialList);
		view.addObject("pageTitle", "BookCentric - Testimonial");
		
		return view;
	}
	
	@PostMapping("/testimonials/add")
	public String saveTestimonial(Testimonial testimonial) {
		testimonialService.save(testimonial);
		
		return "redirect:/testimonials/view";
	}
	
	@GetMapping("/testimonials/delete/{id}")
	public String deleteTestimonial(@PathVariable(name="id") Integer id) {
		Testimonial testimonial = testimonialService.findBy(id);
		testimonialService.delete(testimonial);
		
		return "redirect:/testimonials/view";
	}
	
	@ResponseBody
	@GetMapping("/open/testimonials")
	public Response getPublishedTestimonials() {
		List<Testimonial> testimonials = testimonialService.findAll()
				.stream()
				.filter(t -> t.isPublished() == true)
				.sorted(Comparator.comparing(Testimonial::getId).reversed())
				.collect(Collectors.toList());
		
		Response response = new Response();
		response.setSuccess(true);
		response.setData(testimonials);

		return response;
	}
}
