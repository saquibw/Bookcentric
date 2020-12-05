package com.bookcentric.component.subscription;

import java.util.List;

import com.bookcentric.component.subscription.category.Category;

public interface SubscriptionService {
	
	public Subscription findBy(Integer id);
	
	public void save(Subscription subscription);
	
	public List<Subscription> findAll();
	
	public void delete(Subscription subscription);
	
	public void deleteBy(Integer id);
	
	public List<SubscriptionResponse> getSubscriptionPlansData(List<Subscription> subscriptionList, List<Category> categoryList);
	
	public List<SubscriptionResponse> getSubscriptionChildrenPlansData(List<Subscription> subscriptionList, List<Category> categoryList);
	
	public List<SubscriptionResponse> getSubscriptionFamilyPlansData(List<Subscription> subscriptionList, List<Category> categoryList);

}
