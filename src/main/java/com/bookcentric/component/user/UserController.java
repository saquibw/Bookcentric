package com.bookcentric.component.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bookcentric.component.books.BookService;
import com.bookcentric.component.books.Books;
import com.bookcentric.component.user.deliveryarea.DeliveryArea;
import com.bookcentric.component.user.deliveryarea.DeliveryAreaService;
import com.bookcentric.component.user.parent.Parent;
import com.bookcentric.component.user.paymentmode.PaymentMode;
import com.bookcentric.component.user.paymentmode.PaymentModeService;
import com.bookcentric.component.user.security.UserSecurityService;
import com.bookcentric.component.user.subscription.Subscription;
import com.bookcentric.component.user.subscription.SubscriptionService;
import com.bookcentric.custom.util.Constants;
import com.bookcentric.custom.util.Response;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

@Controller
public class UserController {

	@Autowired private UserService userService;

	@Autowired private SubscriptionService subscriptionService;

	@Autowired private DeliveryAreaService deliveryAreaService;

	@Autowired private PaymentModeService paymentModeService;

	@Autowired BookService bookService;

	@Autowired UserSecurityService userSecurityService;


	@GetMapping("/user/registration")
	public ModelAndView viewRegistration() {
		ModelAndView regView = new ModelAndView("user-registration");

		List<Subscription> subscriptionList = subscriptionService.findAll();
		List<DeliveryArea> deliveryAreaList = deliveryAreaService.findAll();
		List<PaymentMode> paymentModeList = paymentModeService.findAll();

		regView.addObject("pageTitle", "Registration page");
		regView.addObject("user", new User());
		regView.addObject("subscriptionList", subscriptionList);
		regView.addObject("areaList", deliveryAreaList);
		regView.addObject("paymentModeList", paymentModeList);

		return regView;	
	}

	@PostMapping(value="/user/add")
	public String addUser(User user) throws MySQLIntegrityConstraintViolationException {

		user.setStatus(Constants.STATUS_PENDING);
		userService.add(user);

		return "redirect:/user/registration";
	}

	@GetMapping("/user/get/{id}")
	public ModelAndView getUser(@PathVariable("id") int id) {
		ModelAndView editView = new ModelAndView("user-update");
		Optional<User> u = userService.getBy(id);
		User user = u.get();

		if(user.getParent() == null) {
			user.setParent(new Parent());
		}

		List<Subscription> subscriptionList = subscriptionService.findAll();
		List<DeliveryArea> deliveryAreaList = deliveryAreaService.findAll();
		List<PaymentMode> paymentModeList = paymentModeService.findAll();

		editView.addObject("user", user);
		editView.addObject("subscriptionList", subscriptionList);
		editView.addObject("areaList", deliveryAreaList);
		editView.addObject("paymentModeList", paymentModeList);

		return editView;
	}

	@PostMapping("/user/update")
	public String updateUser(User user) throws MySQLIntegrityConstraintViolationException {
		userService.add(user);

		return "redirect:/management/user";
	}

	@ResponseBody
	@RequestMapping(value="/user/updatestatus", method=RequestMethod.POST)
	public Response updateStatus(@RequestParam("status") String status, @RequestParam("id") Integer id) {
		Response response = new Response();

		if(userService.updateStatus(id, status)) {
			response.setSuccess(true);
		}

		return response;

	}

	@ResponseBody
	@PostMapping("/user/update/readingqueue")
	public Response updateReadingQueue(@RequestParam("bookId") Integer bookId, @RequestParam("action") String action) throws MySQLIntegrityConstraintViolationException {
		Response response = new Response();
		String message = "";

		Books book = bookService.getBy(bookId);
		User user = userSecurityService.getLoggedInUser();;
		List<Books> readingQueue = user.getReadingQueue();

		if(Constants.ACTION_ADD.equals(action)) {
			if(readingQueue.size() < 10) {
				readingQueue.add(book);
				user.setReadingQueue(readingQueue);
				userService.add(user);				
				response.setSuccess(true);
			} else {
				message = "You have already added 10 books in your reading queue!";
				response.setSuccess(false);
				response.setData(message);
			}

		} else {
			if(readingQueue.contains(book)) {
				readingQueue.remove(book);
				user.setReadingQueue(readingQueue);
				userService.add(user);				
				response.setSuccess(true);
			}
		}

		return response;		
	}

	@ResponseBody
	@PostMapping("/user/update/wishlist")
	public Response updateWishlist(@RequestParam("bookId") Integer bookId, @RequestParam("action") String action) throws MySQLIntegrityConstraintViolationException {
		Response response = new Response();

		Books book = bookService.getBy(bookId);
		//User user = userService.getBy(16).get();
		User user = userSecurityService.getLoggedInUser();
		List<Books> wishlist = user.getWishlist();

		if(Constants.ACTION_ADD.equals(action)) {

			wishlist.add(book);
			user.setWishlist(wishlist);
			userService.add(user);				
			response.setSuccess(true);
		} else {
			if(wishlist.contains(book)) {
				wishlist.remove(book);
				user.setWishlist(wishlist);
				userService.add(user);				
				response.setSuccess(true);
			}
		}

		return response;		
	}
}
