package com.bookcentric.component.books.author;

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

import com.bookcentric.component.user.security.UserSecurityService;

@Controller
public class AuthorController {
	@Autowired AuthorService authorService;
	
	@GetMapping({"/author/view", "/author/edit/{id}"})
	public ModelAndView authorView(@PathVariable(name="id", required=false) Integer id) {
		ModelAndView view = new ModelAndView("author");
		
		Author author = new Author();
		
		if(id != null && id > 0) {
			author = authorService.findBy(id);
		}
		
		view.addObject("author", author);
		view.addObject("pageTitle", "BookCentric - Author");
		
		return view;
	}
	
	@PostMapping("/author/add")
	public String saveAuthor(Author author) {
		authorService.save(author);
		
		return "redirect:/author/view";
	}
	
	@GetMapping("/author/delete/{id}")
	public String deleteAuthor(@PathVariable(name="id") Integer id) {
		Author author = authorService.findBy(id);
		authorService.delete(author);
		
		return "redirect:/author/view";
	}
	
	private List<Author> sortByName(List<Author> list) {
		return list.stream().sorted(Comparator.comparing(Author::getName)).collect(Collectors.toList());
	}
	
	@ResponseBody
	@GetMapping("/author/get")
	public List<Author> getBySearchtext(@RequestParam String searchText) {
		List<Author> authorList = sortByName(authorService.findAll());
		
		if(!searchText.isEmpty()) {
			if(authorList != null && authorList.size() > 0) {
				authorList = authorList.stream()
				.filter(author -> author.getName().toLowerCase().contains(searchText.toLowerCase()))
				.collect(Collectors.toList());
			}
		}
		return authorList;
	}
}
