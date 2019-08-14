package com.bookcentric.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bookcentric.component.user.UserService;
import com.bookcentric.component.user.history.UserHistoryService;

@Component
public class ScheduledTasks {
	@Autowired UserService userService;
	@Autowired UserHistoryService userHistoryService;
	
	@Scheduled(fixedRate = 10000)
	public void scheduledTaskEveryDay() {
		System.out.println("Scheduled task run");
		//userHistoryService.sendPlanExpiryEmail();
		userService.sendSubscriptionExpiryEmail();
	}
}
