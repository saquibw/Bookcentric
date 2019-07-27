package com.bookcentric.component.subscription;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
	
	@Autowired private SubscriptionRepository repository;

	

	@Override
	public Subscription findBy(Integer id) {
		return repository.getOne(id);
	}

	@Override
	public void save(Subscription subscription) {
		repository.save(subscription);
	}
	
	@Override
	public List<Subscription> findAll() {
		return repository.findAll();
	}

	@Override
	public void delete(Subscription subscription) {
		repository.delete(subscription);
	}

}
