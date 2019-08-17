package com.bookcentric.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bookcentric.component.user.UserService;
import com.bookcentric.component.user.history.UserHistoryService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ScheduledTasks {
	@Autowired UserService userService;
	@Autowired UserHistoryService userHistoryService;
	
	@Scheduled(cron = "0 5 6 * * ?") //Everyday UTC 06:05:00 which means BST (Bangladeshi time) 00:05:00
	public void scheduledTaskEveryDay() {
		log.info("Scheduled tasks run");
		userHistoryService.sendPlanExpiryEmail();
		userService.sendSubscriptionExpiryEmail();
		userService.renewSubscriptionDate();
	}
}
