package com.bookcentric.component.books.publisher;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PublisherController {
	@Autowired PublisherService publisherService;
	
	@GetMapping({"/publisher/view", "/publisher/edit/{id}"})
	public ModelAndView publisherView(@PathVariable(name="id", required=false) Integer id) {
		ModelAndView view = new ModelAndView("publisher");
		
		Publisher publisher = new Publisher();
		
		if(id != null && id > 0) {
			publisher = publisherService.findBy(id);
		}
		List<Publisher> publisherList = publisherService.findAll();
		
		view.addObject("publisher", publisher);
		view.addObject("publisherList", publisherList);
		view.addObject("pageTitle", "BookCentric - Publisher");
		
		return view;
	}
	
	@PostMapping("/publisher/add")
	public String savePublisher(Publisher publisher) {
		publisherService.save(publisher);
		
		return "redirect:/publisher/view";
	}
	
	@GetMapping("/publisher/delete/{id}")
	public String deletePublisher(@PathVariable(name="id") Integer id) {
		Publisher publisher = publisherService.findBy(id);
		publisherService.delete(publisher);
		
		return "redirect:/publisher/view";
	}
}
