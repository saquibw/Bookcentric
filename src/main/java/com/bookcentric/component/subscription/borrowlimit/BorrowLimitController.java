package com.bookcentric.component.subscription.borrowlimit;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bookcentric.custom.util.Response;

@Controller
public class BorrowLimitController {
	
	@Autowired BorrowLimitService borrowLimitService;
	
	@GetMapping({"/borrowlimit/view", "/borrowlimit/edit/{id}"})
	public ModelAndView viewBorrowLimitPage(@PathVariable(name="id", required=false) Integer id) {
		ModelAndView view = new ModelAndView("subscription-borrow-limit");
		
		BorrowLimit borrowLimit = new BorrowLimit();
		
		if(id != null && id > 0) {
			borrowLimit = borrowLimitService.findBy(id);
		}
		List<BorrowLimit> borrowLimitList = borrowLimitService.findAll();
		
		view.addObject("borrowLimit", borrowLimit);
		view.addObject("borrowLimitList", borrowLimitList);
		view.addObject("pageTitle", "BookCentric - Borrow Limit");
		
		return view;
	}
	
	@PostMapping("/borrowlimit/add")
	public String saveBorrowLimit(BorrowLimit borrowLimit) {
		borrowLimitService.save(borrowLimit);
		
		return "redirect:/borrowlimit/view";
	}
	
	@ResponseBody
	@PostMapping("/borrowlimit/delete")
	public Response deleteBorrowLimit(@RequestParam("id") Integer id) {
		borrowLimitService.deleteBy(id);
		
		Response response = new Response();
		response.setSuccess(true);

		return response;
	}
}
