package com.bookcentric.readingchallenge;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ReadingChallengeController {
	
	@Autowired ReadingChallengeService readingChallengeService;

	@GetMapping("/reading-challenge")
	public ModelAndView getView() {
		ModelAndView view = new ModelAndView("reading-challenge");
		
		view.addObject("readingChallenge", new ReadingChallenge());
		view.addObject("readingChallenges", readingChallengeService.getAll());
		view.addObject("pageTitle", "BookCentric - Reading challenge management");
		return view;
	}
	
	@PostMapping("/reading-challenge")
	public String save(ReadingChallenge readingChallenge, @RequestParam("file") MultipartFile file) throws IOException {
		readingChallengeService.save(readingChallenge);
		
		if(file != null && file.getSize() > 0) {
			readingChallengeService.saveImage(file, readingChallenge.getId());
		}
		
		return "redirect:/reading-challenge";
	}
}
