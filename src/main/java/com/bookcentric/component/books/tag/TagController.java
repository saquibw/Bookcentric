package com.bookcentric.component.books.tag;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TagController {
@Autowired TagService tagService;
	
	@GetMapping({"/tag/view", "/tag/edit/{id}"})
	public ModelAndView tagView(@PathVariable(name="id", required=false) Integer id) {
		ModelAndView view = new ModelAndView("tag");
		
		Tag tag = new Tag();
		
		if(id != null && id > 0) {
			tag = tagService.findBy(id);
		}
		List<Tag> tagList = tagService.findAll();
		
		view.addObject("tag", tag);
		view.addObject("tagList", tagList);
		view.addObject("pageTitle", "BookCentric - Tag");
		
		return view;
	}
	
	@PostMapping("/tag/add")
	public String saveTag(Tag tag) {
		tagService.save(tag);
		
		return "redirect:/tag/view";
	}
	
	@GetMapping("/tag/delete/{id}")
	public String deleteTag(@PathVariable(name="id") Integer id) {
		Tag tag = tagService.findBy(id);
		tagService.delete(tag);
		
		return "redirect:/tag/view";
	}
}
