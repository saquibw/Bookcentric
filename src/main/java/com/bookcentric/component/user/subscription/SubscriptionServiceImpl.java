package com.bookcentric.component.user.subscription;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
	
	@Autowired
	private SubscriptionRepository subscriptionRepository;

	@Override
	public List<Subscription> findAll() {
		return subscriptionRepository.findAll();
	}

}
