package com.bookcentric.component.books.publisher;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bookcentric.custom.util.Response;

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
		
		view.addObject("publisher", publisher);
		view.addObject("pageTitle", "BookCentric - Publisher");
		
		return view;
	}
	
	@PostMapping("/publisher/add")
	public String savePublisher(Publisher publisher) {
		publisherService.save(publisher);
		
		return "redirect:/publisher/view";
	}
	
	@ResponseBody
	@PostMapping("/publisher/delete")
	public Response deletePublisher(@RequestParam("id") Integer id) {
		publisherService.deleteBy(id);
		
		Response response = new Response();
		response.setSuccess(true);

		return response;
	}
	
	private List<Publisher> sortByName(List<Publisher> list) {
		return list.stream().sorted(Comparator.comparing(Publisher::getName)).collect(Collectors.toList());
	}
	
	@ResponseBody
	@GetMapping("/publisher/get")
	public List<Publisher> getBySearchtext(@RequestParam String searchText) {
		List<Publisher> publisherList = sortByName(publisherService.findAll());
		
		if(!searchText.isEmpty()) {
			if(publisherList != null && publisherList.size() > 0) {
				publisherList = publisherList.stream()
				.filter(publisher -> publisher.getName().toLowerCase().contains(searchText.toLowerCase()))
				.collect(Collectors.toList());
			}
		}
		return publisherList;
	}
}
