package com.bookcentric.component.books.genre;

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

import com.bookcentric.component.books.author.Author;

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
		
		view.addObject("genre", genre);
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
	
	private List<Genre> sortByName(List<Genre> list) {
		return list.stream().sorted(Comparator.comparing(Genre::getName)).collect(Collectors.toList());
	}
	
	@ResponseBody
	@GetMapping("/genre/get")
	public List<Genre> getBySearchtext(@RequestParam String searchText) {
		List<Genre> genreList = sortByName(genreService.findAll());
		
		if(!searchText.isEmpty()) {
			if(genreList != null && genreList.size() > 0) {
				genreList = genreList.stream()
				.filter(genre -> genre.getName().toLowerCase().contains(searchText.toLowerCase()))
				.collect(Collectors.toList());
			}
		}
		return genreList;
	}
}
