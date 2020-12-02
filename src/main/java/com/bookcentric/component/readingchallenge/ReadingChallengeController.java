package com.bookcentric.component.readingchallenge;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
	@Autowired HttpServletResponse response;

	@GetMapping({"/reading-challenge/management/view", "/reading-challenge/management/edit/{id}"})
	public ModelAndView getManagementView(@PathVariable(name="id", required=false) Integer id) {
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
		view.addObject("pageTitle", "BookCentric - Reading Challenge");
		return view;
	}
	
	@GetMapping("reading-challenge/management/delete/{id}")
	public String deleteReadingChallenge(@PathVariable(name="id") Integer id) {
		readingChallengeService.deleteBy(id);
		
		return "redirect:/reading-challenge/management/view";
	}
	
	@PostMapping("/reading-challenge/management")
	public String save(@ModelAttribute ReadingChallenge readingChallenge, @RequestParam("file") MultipartFile file) throws IOException {

		if (readingChallenge.getBooks() != null && readingChallenge.getBooks().size() > 0) {
			readingChallenge.setBooks(readingChallenge.getBooks()
					.stream()
					.filter(b -> b.getName() != null && b.getAuthor() != null)
					.collect(Collectors.toList()));
			readingChallenge.getBooks().forEach(b -> b.setReadingChallenge(readingChallenge));
		}
		
		readingChallengeService.save(readingChallenge);
		
		
		if(file != null && file.getSize() > 0) {
			readingChallengeService.saveImage(file, readingChallenge.getId());
		}
		
		return "redirect:/reading-challenge/management/view";
	}
	
	@ResponseBody
	@PostMapping("/reading-challenge/management/delete/book")
	public Response deleteBook(@RequestParam("bookId") Integer bookId) {
		Response response = new Response();
		readingChallengeService.deleteBook(bookId);
		response.success = true;
		
		return response;
	}
	
	@GetMapping({"/reading-challenge", "/reading-challenge/{id}"})
	public ModelAndView getUserView(@PathVariable(name="id", required=false) Integer id) {
		ModelAndView view = new ModelAndView("reading-challenge");
		
		ReadingChallenge rc = null;
		
		if(id != null && id > 0) {
			rc = readingChallengeService.getBy(id);
		} else {
			rc = readingChallengeService.getLatest();
		} 
		
		if(rc == null) {
			rc = new ReadingChallenge();
		}

		view.addObject("rc", rc);
		view.addObject("pageTitle", "BookCentric - Reading Challenge");
		
		return view;
	}
	
	@GetMapping("/reading-challenge/image/{id}")
	@ResponseBody
	public void getImage(@PathVariable Integer id) throws SQLException, IOException {
		response.setContentType("image/jpeg");

		byte[] image = readingChallengeService.getImageBy(id);
		if(image != null) {

			ServletOutputStream stream = response.getOutputStream();
			stream.write(image);
			stream.flush();
			stream.close();
		}		
	}
	
	@GetMapping("/reading-challenge/all")
	public ModelAndView getList() {
		ModelAndView mv = new ModelAndView("reading-challenge-list");
		List<ReadingChallenge> rcs = readingChallengeService.getAllPublished();
		
		mv.addObject("rcs", rcs);
		mv.addObject("pageTitle", "BookCentric - Reading Challenge");
		
		return mv;
	}
}
