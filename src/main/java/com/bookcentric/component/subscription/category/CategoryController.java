package com.bookcentric.component.subscription.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bookcentric.component.subscription.borrowlimit.BorrowLimit;
import com.bookcentric.component.subscription.borrowlimit.BorrowLimitService;
import com.bookcentric.component.subscription.planduration.PlanDuration;
import com.bookcentric.component.subscription.planduration.PlanDurationService;

@Controller
public class CategoryController {
	
	@Autowired CategoryService categoryService;
	@Autowired BorrowLimitService borrowLimitService;
	@Autowired PlanDurationService planDurationService;

	@GetMapping({"/category/view", "/category/edit/{id}"})
	public ModelAndView viewCategoryPage(@PathVariable(name="id", required=false) Integer id) {
		ModelAndView view = new ModelAndView("subscription-category");
		
		Category category = new Category();
		
		if(id != null && id > 0) {
			category = categoryService.findBy(id);
		}
		List<Category> categoryList = categoryService.findAll();
		List<BorrowLimit> borrowLimitList = borrowLimitService.findAll();
		List<PlanDuration> planDurationList = planDurationService.findAll();
		
		view.addObject("category", category);
		view.addObject("categoryList", categoryList);
		view.addObject("borrowLimitList", borrowLimitList);
		view.addObject("planDurationList", planDurationList);
		view.addObject("pageTitle", "BookCentric - Category");
		
		return view;
	}
	
	@PostMapping("/category/add")
	public String saveCategory(Category category) {
		System.out.println(category.toString());
		categoryService.save(category);
		
		return "redirect:/category/view";
	}
	
	@GetMapping("/category/delete/{id}")
	public String deleteCategory(@PathVariable(name="id") Integer id) {
		Category category = categoryService.findBy(id);
		categoryService.delete(category);
		
		return "redirect:/category/view";
	}
}
