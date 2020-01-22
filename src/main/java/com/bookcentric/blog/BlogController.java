package com.bookcentric.blog;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bookcentric.component.user.User;
import com.bookcentric.component.user.security.UserSecurityService;
import com.bookcentric.custom.util.Response;

@Controller
public class BlogController {
	
	@Autowired BlogRepository repository;
	@Autowired UserSecurityService userSecurityService;

	@GetMapping({"/blogs/me", "/blog/me/edit/{id}"})
	public ModelAndView viewBlogEditPage(@PathVariable(name="id", required=false) Integer id) {
		ModelAndView mv = new ModelAndView("blog");
		Blog blog = new Blog();
		
		if(id != null && id > 0) {
			blog = repository.getOne(id);
		}

		mv.addObject("blog", blog);
		mv.addObject("pageTitle", "BookCentric - Blog");
		
		return mv;
	}
	
	@PostMapping("/blogs/me/save")
	public String save(Blog blog) {
		User user = userSecurityService.getLoggedInUser();
		blog.setUser(user);
		repository.save(blog);
		
		
		return "redirect:/blog";
	}
	
	@GetMapping("/blogs/all/view")
	public ModelAndView viewBlogList() {
		List<Blog> blogList = repository.findAll();
		
		ModelAndView mv = new ModelAndView("blog-list");
		mv.addObject("blogList", blogList);
		
		return mv;
	}
	
	@ResponseBody
	@GetMapping("/blogs/all")
	public Response getBlogs() {
		List<Blog> blogs = repository.findAll();
		Response response = new Response();
		response.success = true;
		response.data = blogs;
		
		return response;
	}
}
