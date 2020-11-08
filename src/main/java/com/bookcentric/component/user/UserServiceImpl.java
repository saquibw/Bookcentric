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
		String subject = "Your account has been activated";
		
		StringBuilder text = new StringBuilder();
		text.append(String.format("Dear %s", user.getFullName()));
		text.append("<br><br>");
		text.append(String.format("Your account has been activated. Please use password: %s to login here: %s", user.getPassword(), Constants.URL_LOGIN));
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
		text.append(String.format("Dear %s", user.getFullName()));
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
				String expiryDate = user.getDateOfRenewal().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
				
				String subject = String.format("Your subscription (%s) will expire soon", subscriptionPlanName);
				
				StringBuilder text = new StringBuilder();
				text.append(String.format("Dear %s", user.getFullName()));
				text.append("<br><br>");
				text.append(String.format("This is a gentle reminder that your monthly subscription (%s) is due for renewal on the %s. ", subscriptionPlanName, expiryDate));
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
		text.append(String.format("Dear %s", user.getFullName()));
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
