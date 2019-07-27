package com.bookcentric.component.subscription;

import java.util.List;

public interface SubscriptionService {
	
	public Subscription findBy(Integer id);
	
	public void save(Subscription subscription);
	
	public List<Subscription> findAll();
	
	public void delete(Subscription subscription);

}
