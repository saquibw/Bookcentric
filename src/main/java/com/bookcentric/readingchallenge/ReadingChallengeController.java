package com.bookcentric.readingchallenge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.bookcentric.custom.util.Response;

@Controller
public class ReadingChallengeController {
	
	@Autowired ReadingChallengeService readingChallengeService;

	@GetMapping({"/reading-challenge/view", "/reading-challenge/edit/{id}"})
	public ModelAndView getView(@PathVariable(name="id", required=false) Integer id) {
		ModelAndView view = new ModelAndView("reading-challenge-management");
		
		ReadingChallenge rc = new ReadingChallenge();
		
		if(id != null && id > 0) {
			rc = readingChallengeService.getBy(id);
		} 
		
		if(rc.getBooks() == null) {
			List<ReadingChallengeBook> books = new ArrayList<ReadingChallengeBook>();
			rc.setBooks(books);
		}
		
		view.addObject("readingChallenge", rc);
		view.addObject("readingChallenges", readingChallengeService.getAll());
		view.addObject("pageTitle", "BookCentric - Reading challenge management");
		return view;
	}
	
	@GetMapping("reading-challenge/delete/{id}")
	public String deleteReadingChallenge(@PathVariable(name="id") Integer id) {
		readingChallengeService.deleteBy(id);
		
		return "redirect:/reading-challenge/view";
	}
	
	@PostMapping("/reading-challenge")
	public String save(@ModelAttribute ReadingChallenge readingChallenge, @RequestParam("file") MultipartFile file) throws IOException {

		readingChallenge.setBooks(readingChallenge.getBooks()
		.stream()
		.filter(b -> b.getName() != null && b.getAuthor() != null)
		.collect(Collectors.toList()));
		readingChallenge.getBooks().forEach(b -> b.setReadingChallenge(readingChallenge));
		
		readingChallengeService.save(readingChallenge);
		
		if(file != null && file.getSize() > 0) {
			readingChallengeService.saveImage(file, readingChallenge.getId());
		}
		
		return "redirect:/reading-challenge/view";
	}
	
	@ResponseBody
	@PostMapping("/reading-challenge/delete/book")
	public Response deleteBook(@RequestParam("bookId") Integer bookId) {
		Response response = new Response();
		readingChallengeService.deleteBook(bookId);
		response.success = true;
		
		return response;
	}
}
