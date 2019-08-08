package com.bookcentric.component.books.genre;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GenreController {
	@Autowired GenreService genreService;
	
	@GetMapping({"/genre/view", "/genre/edit/{id}"})
	public ModelAndView genreView(@PathVariable(name="id", required=false) Integer id) {
		ModelAndView view = new ModelAndView("genre");
		
		Genre genre = new Genre();
		
		if(id != null && id > 0) {
			genre = genreService.findBy(id);
		}
		List<Genre> genreList = genreService.findAll();
		
		view.addObject("genre", genre);
		view.addObject("genreList", genreList);
		view.addObject("pageTitle", "BookCentric - Genre");
		
		return view;
	}
	
	@PostMapping("/genre/add")
	public String saveGenre(Genre genre) {
		genreService.save(genre);
		
		return "redirect:/genre/view";
	}
	
	@GetMapping("/genre/delete/{id}")
	public String deleteGenre(@PathVariable(name="id") Integer id) {
		Genre genre = genreService.findBy(id);
		genreService.delete(genre);
		
		return "redirect:/genre/view";
	}
}
