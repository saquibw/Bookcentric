package com.bookcentric.component.books.review;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bookcentric.component.user.User;
import com.bookcentric.component.user.security.UserSecurityService;
import com.bookcentric.custom.util.Response;

@Controller
public class ReviewController {
	
	@Autowired ReviewRepository repository;
	@Autowired UserSecurityService userSecurityService;
	@Autowired ModelMapper mapper;

	@ResponseBody
	@GetMapping("/reviews/self")
	public Response getReviewListByBook(@RequestParam Integer bookId) {
		Response response = new Response();
		
		User user = userSecurityService.getLoggedInUser();		
		
		Review review = repository.findOneByBookIdAndUserId(bookId, user.getId());
		ReviewDTO dto = null;
		if(review != null) {
			dto = mapper.map(review, ReviewDTO.class);
		}
				
		response.success = true;
		response.setData(dto);
		
		return response;
	}
	
	@ResponseBody
	@PutMapping("/reviews/self/rating")
	public Response updateSelfRating(@RequestParam Integer bookId, @RequestParam Integer rating) {
		Response response = new Response();
		
		User user = userSecurityService.getLoggedInUser();	
		Review review = repository.findOneByBookIdAndUserId(bookId, user.getId());
		
		if(review == null) {
			review = new Review();
			review.setBookId(bookId);
			review.setUser(user);
			review.setComment("");
		}

		review.setRating(rating);
		repository.save(review);
		
		ReviewDTO dto = mapper.map(review, ReviewDTO.class);
		
		response.success = true;
		response.setData(dto);
		
		return response;
	}
	
	@ResponseBody
	@PutMapping("/reviews/self/comment")
	public Response updateSelfComment(@RequestParam Integer bookId, @RequestParam String comment) {
		Response response = new Response();
		
		User user = userSecurityService.getLoggedInUser();	
		Review review = repository.findOneByBookIdAndUserId(bookId, user.getId());
		
		if(review == null) {
			review = new Review();
			review.setBookId(bookId);
			review.setUser(user);			
		}

		review.setComment(comment);
		repository.save(review);
		
		ReviewDTO dto = mapper.map(review, ReviewDTO.class);
		
		response.success = true;
		response.setData(dto);
		
		return response;
	}
	
	@ResponseBody
	@GetMapping("/reviews/other")
	public Response getOtherReviews(@RequestParam Integer bookId) {
		Response response = new Response();
		
		List<Review> list = repository.findAllByBookIdOrderByModifiedAtDesc(bookId);
		
		User user = userSecurityService.getLoggedInUser();	
		/*if(user != null) {
			list = list.stream().filter(r -> r.getUser().getId() != user.getId()).collect(Collectors.toList());
		}*/
		
		List<ReviewDTO> reviewList = list.stream().map(l -> {
			return mapper.map(l, ReviewDTO.class);
		}).collect(Collectors.toList());
		
		response.setSuccess(true);
		response.setData(reviewList);
		
		return response;
	}
}
