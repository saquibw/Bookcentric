package com.bookcentric.component.blog;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.bookcentric.component.user.User;
import com.bookcentric.component.user.security.UserSecurityService;
import com.bookcentric.custom.util.Constants;
import com.bookcentric.custom.util.Response;

@Controller
public class BlogController {

	@Autowired BlogRepository repository;
	@Autowired UserSecurityService userSecurityService;
	@Autowired ModelMapper mapper;
	@Autowired HttpServletResponse response;
	@Autowired BlogCommentsRepository commentRepository;

	@GetMapping({"/blogs/me", "/blogs/me/edit/{id}"})
	public ModelAndView viewBlogEditPage(@PathVariable(name="id", required=false) Integer id) {
		ModelAndView mv = new ModelAndView("blog");
		Blog blog = new Blog();

		if(id != null && id > 0) {
			User user = userSecurityService.getLoggedInUser();
			blog = repository.getOne(id);
			if(!user.getId().equals(blog.getUser().getId())) {
				blog = new Blog();
			}
		}

		mv.addObject("blog", blog);
		mv.addObject("pageTitle", "BookCentric - Thoughts & Musings");

		return mv;
	}

	@PostMapping("/blogs/me/save")
	public String save(Blog blog, @RequestParam("file") MultipartFile file) {
		User user = userSecurityService.getLoggedInUser();
		blog.setUser(user);
		
		if(!ObjectUtils.isEmpty(blog.getId())) {
			Blog b = repository.getOne(blog.getId());
			blog.setCreatedAt(b.getCreatedAt());
		}
		repository.save(blog);

		if(file != null && file.getSize() > 0) {			
			try {
				byte[] image = file.getBytes();
				repository.updateImageById(image, blog.getId());
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return "redirect:/blogs/me/edit/" + blog.getId().toString();
	}
	
	@ResponseBody
	@DeleteMapping("/blogs/me/{blogId}")
	public Response deleteBlog(@PathVariable Integer blogId) {
		Response response = new Response();
		
		Blog blog = repository.findById(blogId).get();
		
		if(blog != null) {
			repository.delete(blog);
			response.setSuccess(true);
		}
		
		return response;		
	}

	@GetMapping("/blogs/all/view")
	public ModelAndView viewBlogList() {
		ModelAndView mv = new ModelAndView("blog-list");
		mv.addObject("pageTitle", "BookCentric - Thoughts & Musings");

		return mv;
	}
	
	@GetMapping("/blogs/all/view/{id}")
	public ModelAndView viewBlogItem(@PathVariable(name="id") Integer id) {
		Blog blog = repository.findById(id).get();

		ModelAndView mv = new ModelAndView("blog-view");
		mv.addObject("blog", blog);
		mv.addObject("pageTitle", "BookCentric - Thoughts & Musings");

		return mv;
	}

	@ResponseBody
	@GetMapping("/blogs/all")
	public Response getBlogs(@RequestParam boolean isOwnBlogs) {
		User user = userSecurityService.getLoggedInUser();
		List<Blog> blogs = new ArrayList<>();
		
		if(isOwnBlogs) {
			blogs = repository.findAllById(user.getId());
		} else {
			blogs = repository.findAllByPublished();
		}
		
		List<BlogResponse> blogResponses = blogs.stream()
				.map(b -> {
					BlogResponse br = new BlogResponse();
					br = mapper.map(b, BlogResponse.class);					
					br.setUserFullName(b.getUser().getFullName());
					br.setCreatedAtText(b.getCreatedAt().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
					br.setAdmin(!ObjectUtils.isEmpty(user) && user.getRole().equals("admin") ? true : false);
					br.setOwn(!ObjectUtils.isEmpty(user) && user.getId().equals(b.getUser().getId()) ? true : false);
					br.setCommentCount(b.getComments().size());
					return br;
				}).collect(Collectors.toList());

		Response response = new Response();
		response.success = true;
		response.data = blogResponses;

		return response;
	}

	@GetMapping("/blogs/all/image/{id}")
	@ResponseBody
	public void getImage(@PathVariable Integer id) throws SQLException, IOException {
		response.setContentType("image/jpeg");

		byte[] image = repository.getImageById(id);
		if(image != null) {

			ServletOutputStream stream = response.getOutputStream();
			stream.write(image);
			stream.flush();
			stream.close();
		}		
	}
	
	@ResponseBody
	@PutMapping("/blogs/me/comments")
	public Response updateSelfComment(@RequestParam Integer blogId, @RequestParam Integer commentId, @RequestParam String comment) {
		Response response = new Response();
		
		Blog blog = repository.getOne(blogId);
		User user = userSecurityService.getLoggedInUser();
		BlogComments blogComment = new BlogComments();
		
		if(commentId == null) {
			blogComment.setBlog(blog);
			blogComment.setUser(user);
		} else {
			blogComment = commentRepository.getOne(commentId);
		}

		blogComment.setComment(comment);
		commentRepository.save(blogComment);
		
		response.success = true;
		
		return response;
	}
	
	@ResponseBody
	@DeleteMapping("/blogs/me/comments/{commentId}")
	public Response deleteReview(@PathVariable Integer commentId) {
		Response response = new Response();		
		BlogComments blogComment = commentRepository.getOne(commentId);
		
		if(blogComment != null) {
			commentRepository.delete(blogComment);
			response.setSuccess(true);
		}
		
		return response;		
	}
	
	@ResponseBody
	@GetMapping("/blogs/all/comments")
	public Response getOtherReviews(@RequestParam Integer blogId) {
		Response response = new Response();
		
		List<BlogComments> list = commentRepository.findAllByBlogIdOrderByCreatedAtDesc(blogId);
				
		response.setSuccess(true);
		response.setData(list);
		
		User user = userSecurityService.getLoggedInUser();
		if(user != null) {
			if(user.getId() != null) response.setLoggedinUserId(user.getId());
			if(user.getRole() != null && !user.getRole().isEmpty() && user.getRole().equals(Constants.ROLE_ADMIN)) response.setAdmin(true);
		}		
		
		return response;
	}
}
