package com.bookcentric.component.user;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.bookcentric.component.books.BookService;
import com.bookcentric.component.books.Books;
import com.bookcentric.component.subscription.Subscription;
import com.bookcentric.component.subscription.SubscriptionService;
import com.bookcentric.component.user.deliveryarea.DeliveryArea;
import com.bookcentric.component.user.deliveryarea.DeliveryAreaService;
import com.bookcentric.component.user.parent.Parent;
import com.bookcentric.component.user.paymentmode.PaymentMode;
import com.bookcentric.component.user.paymentmode.PaymentModeService;
import com.bookcentric.component.user.security.UserSecurityService;
import com.bookcentric.component.user.status.UserStatus;
import com.bookcentric.component.user.status.UserStatusService;
import com.bookcentric.component.utils.UtilService;
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
	
	@Autowired UserStatusService userStatusService;
	
	@Autowired UtilService utilService;
	
	@Autowired HttpServletResponse response;


	@GetMapping("/user/registration")
	public ModelAndView viewRegistration() {
		return getUserRegModel();
	}

	@PostMapping(value="/user/add")
	public ModelAndView addUser(User user) throws MySQLIntegrityConstraintViolationException {
		ModelAndView mv = getUserRegModel();
		
		List<User> userList = userService.getAll();
		
		if(user != null && !user.getEmail().isEmpty()) {
			List<User> duplicateEmailList = userList.stream().
					filter(u -> u.getEmail().equals(user.getEmail()))
					.collect(Collectors.toList());
			if(duplicateEmailList != null && duplicateEmailList.size() > 0) {
				mv.addObject("user", user);
				mv.addObject("duplicateEmail", "true");
			} else {
				UserStatus status = userStatusService.getBy(3);
				user.setStatus(status);
				user.setRole(Constants.ROLE_USER);
				user.setCreatedAt(LocalDateTime.now());
				
				userService.add(user);
				
				userService.sendUserRegistrationEmail(user);
				
				mv.addObject("registrationSuccess", "true");
			}
		}

		return mv;
	}
	
	private ModelAndView getUserRegModel() {
		ModelAndView regView = new ModelAndView("user-registration");

		List<Subscription> subscriptionList = subscriptionService.findAll();
		List<DeliveryArea> deliveryAreaList = deliveryAreaService.findAll();
		List<PaymentMode> paymentModeList = paymentModeService.findAll();
		List<UserStatus> userStatusList = userStatusService.findAll();

		regView.addObject("pageTitle", "Registration page");
		regView.addObject("user", new User());
		regView.addObject("subscriptionList", subscriptionList);
		regView.addObject("areaList", deliveryAreaList);
		regView.addObject("paymentModeList", paymentModeList);
		regView.addObject("userStatusList", userStatusList);

		return regView;
	}

	@GetMapping({"/user/get/{id}", "/user/get/{id}/{self}"})
	public ModelAndView getUser(@PathVariable("id") int id, @PathVariable(name="self", required=false) boolean self) {
		ModelAndView editView = new ModelAndView("user-update");
		
		if(self) {
			editView.setViewName("user-self-update");
		}
		
		User user = userService.getBy(id);

		if(user.getParent() == null) {
			user.setParent(new Parent());
		}

		List<Subscription> subscriptionList = subscriptionService.findAll();
		List<DeliveryArea> deliveryAreaList = deliveryAreaService.findAll();
		List<PaymentMode> paymentModeList = paymentModeService.findAll();
		List<UserStatus> userStatusList = userStatusService.findAll();

		editView.addObject("user", user);
		editView.addObject("subscriptionList", subscriptionList);
		editView.addObject("areaList", deliveryAreaList);
		editView.addObject("paymentModeList", paymentModeList);
		editView.addObject("userStatusList", userStatusList);
		

		return editView;
	}

	@PostMapping("/user/update")
	public String updateUser(User user) throws MySQLIntegrityConstraintViolationException {
		User dbUser = userService.getBy(user.getId());
		
		user.setReadingQueue(dbUser.getReadingQueue());
		user.setWishlist(dbUser.getWishlist());
		user.setPassword(dbUser.getPassword());
		user.setRole(dbUser.getRole());
		user.setCreatedAt(dbUser.getCreatedAt());
				
		if((dbUser.getPassword() == null || dbUser.getPassword().isEmpty()) && Constants.STATUS_ACTIVE.toLowerCase().equals(user.getStatus().getName().toLowerCase())) {
			String password = utilService.getAlphaNumericString(6);
			String encryptedPassword = utilService.encryptPassword(password);
			user.setPassword(encryptedPassword);
			 
			dbUser.setPassword(password);
			userService.sendUserActivationEmail(dbUser);
		} else if (!user.getStatus().getName().equalsIgnoreCase(dbUser.getStatus().getName())) {
			userService.sendUserStatusUpdateEmail(user);
		} 
		
		userService.add(user);
		
		return "redirect:/management/user";
	}
	
	@GetMapping("/user/me")
	public ModelAndView getSelfUser() {
		User user = userSecurityService.getLoggedInUser();
		
		ModelAndView editView = new ModelAndView("user-self-update");
		
		//User user = userService.getBy(id);

		if(user.getParent() == null) {
			user.setParent(new Parent());
		}

		List<Subscription> subscriptionList = subscriptionService.findAll();
		List<DeliveryArea> deliveryAreaList = deliveryAreaService.findAll();
		List<PaymentMode> paymentModeList = paymentModeService.findAll();
		List<UserStatus> userStatusList = userStatusService.findAll();

		editView.addObject("user", user);
		editView.addObject("subscriptionList", subscriptionList);
		editView.addObject("areaList", deliveryAreaList);
		editView.addObject("paymentModeList", paymentModeList);
		editView.addObject("userStatusList", userStatusList);
		

		return editView;
		
		// return "redirect:/user/get/" + user.getId() + "/true";
	}
	
	@PostMapping("/user/me/update")
	public String updateSelfUser(User user, @RequestParam("file") MultipartFile file) throws MySQLIntegrityConstraintViolationException, IOException {
		User dbUser = userService.getBy(user.getId());
		
		user.setReadingQueue(dbUser.getReadingQueue());
		user.setWishlist(dbUser.getWishlist());
		user.setPassword(dbUser.getPassword());
		user.setRole(dbUser.getRole());
		user.setCreatedAt(dbUser.getCreatedAt());
		user.setMembershipId(dbUser.getMembershipId());
		user.setStatus(dbUser.getStatus());
		user.setEmail(dbUser.getEmail());
		user.setSubscription(dbUser.getSubscription());
		
		userService.add(user);
		
		if(file != null && file.getSize() > 0) {
			userService.storeImage(file, user.getId());
		}	
		
		return "redirect:/user/me";
	}
	
	@GetMapping("/user/me/image/{id}")
	@ResponseBody
	public void getImage(@PathVariable Integer id) throws SQLException, IOException {
		response.setContentType("image/jpeg");

		byte[] image = userService.getImageBy(id);
		if(image != null) {

			ServletOutputStream stream = response.getOutputStream();
			stream.write(image);
			stream.flush();
			stream.close();
		}		
	}

	@ResponseBody
	@PostMapping("/user/update/readingqueue")
	public Response updateReadingQueue(
			@RequestParam(name="bookId") Integer bookId, 
			@RequestParam(name="action") String action,
			@RequestParam(name="otherUserId", required=false) Integer otherUserId) throws MySQLIntegrityConstraintViolationException {
		Response response = new Response();
		String message = "";

		Books book = bookService.getBy(bookId);
		User user = userSecurityService.getLoggedInUser();
		
		// When an admin adds books to reading queue for another user
		if (otherUserId != null && otherUserId > 0) {
			user = userService.getBy(otherUserId);
		}
		
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
	
	@ResponseBody
	@PostMapping("/user/reset/password")
	public Response resetPasswordByAdmin(@RequestParam("userId") Integer userId) {
		Response response = new Response();
		
		if(userId != null) {
			User user = userService.getBy(userId);
			if(user != null) {
				String password = utilService.getAlphaNumericString(6);
				String encryptedPassword = utilService.encryptPassword(password);
				user.setPassword(encryptedPassword);
				userService.add(user);
				
				userService.sendUserPasswordResetEmail(user, password);
				
				response.setSuccess(true);
			} else {
				response.setSuccess(false);
			}
		} else {
			response.setSuccess(false);
		}
		
		return response;
	}
}
