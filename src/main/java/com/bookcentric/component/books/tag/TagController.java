package com.bookcentric.component.books.tag;

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
	
	@ResponseBody
	@PostMapping("/tag/delete")
	public Response deleteTag(@RequestParam("id") Integer id) {
		tagService.deleteBy(id);
		
		Response response = new Response();
		response.setSuccess(true);

		return response;
	}
	
	private List<Tag> sortByName(List<Tag> list) {
		return list.stream().sorted(Comparator.comparing(Tag::getName)).collect(Collectors.toList());
	}
	
	@ResponseBody
	@GetMapping("/tag/get")
	public List<Tag> getBySearchtext(@RequestParam String searchText) {
		List<Tag> tagList = sortByName(tagService.findAll());
		
		if(!searchText.isEmpty()) {
			if(tagList != null && tagList.size() > 0) {
				tagList = tagList.stream()
				.filter(tag -> tag.getName().toLowerCase().contains(searchText.toLowerCase()))
				.collect(Collectors.toList());
			}
		}
		return tagList;
	}
}
