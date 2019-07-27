package com.bookcentric.component.subscription.subscriptionduration;

import java.util.List;

public interface SubscriptionDurationService {
	
	public SubscriptionDuration findBy(Integer id);
	
	public void save(SubscriptionDuration subscriptionDuration);
	
	public List<SubscriptionDuration> findAll();
	
	public void delete(SubscriptionDuration subscriptionDuration);
}
