package com.bookcentric.component.wishlist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bookcentric.component.user.User;
import com.bookcentric.component.user.UserService;

@Controller
public class WishlistController {

	@Autowired private UserService userService;
	@Autowired private WishlistService wishlistService;

	@GetMapping("/wishlist")
	public ModelAndView viewWishlist() {
		Wishlist wishlist = new Wishlist();
		
		ModelAndView mv = new ModelAndView("wishlist");
		mv.addObject("wishlist", wishlist);
		mv.addObject("pageTitle", "BookCentric - Wishlist");
		
		return mv;
	}

	@PostMapping("/wishlist")
	public ModelAndView addWishlist(Wishlist wishlist) {
		ModelAndView mv = new ModelAndView("wishlist");
		User user = userService.getByMembershipId(wishlist.getMembershipNo());
		if(user == null) {
			mv.addObject("membershipFailure", true);
			mv.addObject("wishlist", wishlist);
		} else {
			wishlistService.sendWishlistEmail(wishlist);
			mv.addObject("wishlistSuccess", true);
			mv.addObject("wishlist", new Wishlist());
		}
		
		mv.addObject("pageTitle", "BookCentric - Wishlist");
		
		return mv;
	}
}
