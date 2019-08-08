package com.bookcentric.component.books.author;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

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
		List<Author> authorList = authorService.findAll();
		
		view.addObject("author", author);
		view.addObject("authorList", authorList);
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
}
