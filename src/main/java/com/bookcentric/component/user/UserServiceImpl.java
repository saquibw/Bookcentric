package com.bookcentric.component.user;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bookcentric.component.utils.EmailService;
import com.bookcentric.config.AppConfig;
import com.bookcentric.custom.util.AppUtil;
import com.bookcentric.custom.util.Constants;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired private UserRepository userRepository;
	
	@Autowired EmailService emailService;
	
	@Autowired AppConfig config;
	
	@Autowired ModelMapper mapper;
	
	public void add(User user) {		
		userRepository.save(user);		
	}

	@Override
	public List<User> getAll() {		
		return userRepository.findAll();
	}

	@Override
	public User getBy(Integer id) {
		User user = userRepository.getOne(id);
		return user;
	}
	
	@Override
	public User getByMembershipId(String id) {
		User user = userRepository.findByMembershipId(id);
		return user;
	}

	@Override
	public boolean sendUserActivationEmail(User user) {
		String to = user.getEmail();
		String subject = "Welcome to Bookcentric";
		
		StringBuilder text = new StringBuilder();
		text.append(String.format("Dear %s,", user.getFullName()));
		text.append("<br><br>");
		text.append("Welcome to Bookcentric! Thank you for signing up with us. ");
		text.append("<br><br>");
		text.append(String.format("Please use your email and password: <b>%s</b> to log into your account. ", user.getPassword()));
		text.append("<br><br>");
		text.append("You will find the following information on the <b>Membership Dashboard</b> :");
		text.append("<br>");
		text.append("<ul>");
		text.append("<li>Membership Number</li><li>Subscription Plan</li><li>Date of Renewal of Subscription</li>");		
		text.append("<li>Library Card : It will tell you which book/books you have currently borrowed and their due dates.</li>");
		text.append("<li>Reading Queue : It is a list of 10 books that you would like to borrow. You can edit the RQ whenever you want.");
		text.append("<ul><li>You can browse through the Treasury to see our collection and choose the books you are interested in.</li>");
		text.append("<li>You can add books to your RQ by clicking on the ‘+’ button under the book.</li></ul></li>");
		text.append("<li>To Be Read  List : It is a list of books that you are interested in reading.");
		text.append("<ul><li>You can add books to the TBR list by clicking on the ‘♥’ button under the book.</li>");
		text.append("<li>You can add books from the TBR list to the RQ by clicking on the ‘+’ button beside the book on the Member Dashboard.</li></ul></li>");
		text.append("<li>Badges : Coming Soon !!</li>");
		text.append("<li>Things to Remember : Please read and follow the information in the Things to Remember to ensure that our books are handled properly.</li>");
		text.append("<br>");
		text.append(String.format("<b>Treasury: %s</b>", Constants.URL_TREASURY));
		text.append("<br>");
		text.append(String.format("<b>Blog: %s</b>", Constants.URL_BLOGS));
		text.append("<br>");
		text.append(String.format("<b>Events: %s</b>", Constants.URL_EVENTS));
		text.append("<br>");
		text.append(String.format("<b>Reading Challenge: %s</b>", Constants.URL_READING_CHALLENGES));
		text.append("<br>");
		text.append(String.format("<b>Gift a Subscription: %s</b>", Constants.URL_GIFT_SUBSCRIPTION));
		text.append("<br>");
		text.append(String.format("<b>Donate Books: %s</b>", Constants.URL_DONATE_BOOKS));
		text.append("<br>");
		text.append(String.format("<b>Recommendations (let us help you): %s</b>", "https://www.facebook.com/bookcentric"));
		text.append("<br>");
		text.append(String.format("<b>Wishlist (request a book): %s</b>", Constants.URL_WISH_FOR_A_BOOK));
		text.append("<br><br>");
		text.append(AppUtil.getEmailSignature());
		
		try {
			emailService.sendHtmlEmail(to, subject, text.toString());
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		return true;
	}

	@Override
	public boolean sendUserStatusUpdateEmail(User user) {
		String to = user.getEmail();
		String subject = "Your account status has been updated";
		
		StringBuilder text = new StringBuilder();
		text.append(String.format("Dear %s,", user.getFullName()));
		text.append("<br><br>");
		text.append(String.format("Your account status has been changed to : %s", user.getStatus().getName()));
		text.append("<br><br>");
		text.append(AppUtil.getEmailSignature());
		
		try {
			emailService.sendHtmlEmail(to, subject, text.toString());
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		return true;
	}

	@Override
	public boolean sendUserRegistrationEmail(User user) {
		String to = config.getEmailRecipient();
		String subject = "A new user has been registered";
		
		StringBuilder text = new StringBuilder();
		text.append(String.format("User %s (%s) has completed registration. Please click below to check.", user.getFullName(), user.getEmail()));
		text.append("\n\n");
		text.append(Constants.URL_MANAGEMENT_USER);
		
		emailService.sendSimpleEmail(to, subject, text.toString());
		
		return true;
	}

	@Async
	@Override
	@Transactional
	public void sendSubscriptionExpiryEmail() {
		List<User> userList = userRepository.findByActiveUserAndSubscriptionExpiry(Constants.EXPIRY_AFTER_DAYS);
		
		if(userList != null && userList.size() > 0) {
			userList.forEach(user -> {
				String to = user.getEmail();
				String subscriptionPlanName = user.getSubscription().getCategory().getName();
				String subscriptionPlanDurationName = user.getSubscription().getSubscriptionDuration().getName().toLowerCase();
				String expiryDate = user.getDateOfRenewal().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
				
				String subject = String.format("Your subscription (%s) will expire soon", subscriptionPlanName);
				
				StringBuilder text = new StringBuilder();
				text.append(String.format("Dear %s,", user.getFullName()));
				text.append("<br><br>");
				text.append(String.format("This is a gentle reminder that your %s subscription (%s) is due for renewal on the %s. ", subscriptionPlanDurationName, subscriptionPlanName, expiryDate));
				text.append("Please let us know if you would like to renew your subscription so we can do the needful. ");
				text.append("If you would like to change your subscription plan at the time of renewal, please let us know and we will make changes accordingly.");
				text.append("<br><br>");
				text.append(AppUtil.getEmailSignature());
				
				try {
					emailService.sendHtmlEmail(to, subject, text.toString());
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			});
		}
	}

	@Async
	@Override
	@Transactional
	public void renewSubscriptionDate() {
		List<User> expiredUsers = userRepository.findAllActiveExpiresToday();
		
		if(expiredUsers != null && expiredUsers.size() > 0) {
			expiredUsers.forEach(u -> {
				int subscriptionDurationInMonths = u.getSubscription().getSubscriptionDuration().getDurationInDays() / 30;
				u.setDateOfRenewal(u.getDateOfRenewal().plusMonths(subscriptionDurationInMonths));
			});
			userRepository.saveAll(expiredUsers);
		}
		
	}

	@Override
	public UserLoginDTO getBy(String email) {
		User user = userRepository.getByEmail(email);
		UserLoginDTO userDto = mapper.map(user, UserLoginDTO.class);
		
		return userDto;
	}

	@Override
	public User getByEmail(String email) {
		return userRepository.getByEmail(email);
	}
	
	@Override
	public boolean sendUserPasswordResetEmail(User user, String password) {
		String to = user.getEmail();
		String subject = "Your password has been reset";
		
		StringBuilder text = new StringBuilder();
		text.append(String.format("Dear %s,", user.getFullName()));
		text.append("<br><br>");
		text.append(String.format("Your password has been reset by admin. Please use password: %s to login here: %s", password, Constants.URL_LOGIN));
		text.append("<br><br>");
		text.append(AppUtil.getEmailSignature());
		
		try {
			emailService.sendHtmlEmail(to, subject, text.toString());
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		return true;
	}

	@Override
	public void storeImage(MultipartFile file, Integer id) throws IOException {
		byte[] image = file.getBytes();
		userRepository.updateImageById(image, id);		
	}
	
	@Override
	public byte[] getImageBy(Integer id) {
		return userRepository.getImageById(id);
	}

}
