package com.bookcentric.component.subscription.subscriptionduration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionDurationServiceImpl implements SubscriptionDurationService {
	
	@Autowired SubscriptionDurationRepository repository;

	@Override
	public SubscriptionDuration findBy(Integer id) {
		return repository.getOne(id);
	}

	@Override
	public void save(SubscriptionDuration subscriptionDuration) {
		repository.save(subscriptionDuration);
	}

	@Override
	public List<SubscriptionDuration> findAll() {
		return repository.findAll();
	}

	@Override
	public void delete(SubscriptionDuration subscriptionDuration) {
		repository.delete(subscriptionDuration);
	}

	@Override
	public void deleteBy(Integer id) {
		repository.deleteById(id);
	}

}
