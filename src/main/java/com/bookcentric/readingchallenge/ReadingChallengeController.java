package com.bookcentric.readingchallenge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ReadingChallengeController {
	
	@Autowired ReadingChallengeService readingChallengeService;

	@GetMapping({"/reading-challenge", "/reading-challenge/{id}"})
	public ModelAndView getView(@PathVariable(name="id", required=false) Integer id) {
		ModelAndView view = new ModelAndView("reading-challenge");
		
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
	
	@PostMapping("/reading-challenge")
	public String save(@ModelAttribute ReadingChallenge readingChallenge, @RequestParam("file") MultipartFile file) throws IOException {
		readingChallenge.getBooks().forEach(b -> b.setReadingChallenge(readingChallenge));
		
		readingChallengeService.save(readingChallenge);
		
		if(file != null && file.getSize() > 0) {
			readingChallengeService.saveImage(file, readingChallenge.getId());
		}
		
		return "redirect:/reading-challenge";
	}
}
