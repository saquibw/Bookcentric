package com.bookcentric.component.subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookcentric.component.subscription.category.Category;

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

	@Override
	public List<SubscriptionResponse> getSubscriptionPlansData(List<Subscription> subscriptionList, List<Category> categoryList) {
		List<SubscriptionResponse> plans = new ArrayList<>();
		
		List<Category> filteredCategoryList = categoryList.stream().filter(c -> !c.isFamily() && !c.isUnderAge()).collect(Collectors.toList());
		
		plans = getFilteredPlans(filteredCategoryList, subscriptionList);
		
		return plans;
	}

	@Override
	public List<SubscriptionResponse> getSubscriptionChildrenPlansData(List<Subscription> subscriptionList, List<Category> categoryList) {
		List<SubscriptionResponse> childrenPlans = new ArrayList<>();
		
		List<Category> filteredCategoryList = categoryList.stream().filter(c -> c.isUnderAge()).collect(Collectors.toList());
		
		childrenPlans = getFilteredPlans(filteredCategoryList, subscriptionList);
		
		return childrenPlans;
	}

	@Override
	public List<SubscriptionResponse> getSubscriptionFamilyPlansData(List<Subscription> subscriptionList, List<Category> categoryList) {
		List<SubscriptionResponse> familyPlans = new ArrayList<>();
		
		List<Category> filteredCategoryList = categoryList.stream().filter(c -> c.isFamily()).collect(Collectors.toList());
		
		familyPlans = getFilteredPlans(filteredCategoryList, subscriptionList);
		
		return familyPlans;
	}

	private List<SubscriptionResponse> getFilteredPlans(List<Category> filteredCategoryList, List<Subscription> subscriptionList) {
		List<SubscriptionResponse> plans = new ArrayList<>();
		if(filteredCategoryList != null && filteredCategoryList.size() > 0) {
			filteredCategoryList.forEach(fc -> {
				SubscriptionResponse response = new SubscriptionResponse();
				response.setName(fc.getName());
				response.setLimitEachTime(fc.getBorrowLimit().getEachTime());
				response.setLimitPerMonth(fc.getBorrowLimit().getPerMonth());
				response.setDuration(fc.getPlanDuration().getName());
				response.setSecurityAmount(fc.getSecurityAmount());
				
				List<Subscription> filteredSubscriptionList = subscriptionList.stream().filter(fs -> fs.getCategory().getId() == fc.getId()).collect(Collectors.toList());

				if(filteredSubscriptionList != null && filteredSubscriptionList.size() > 0) {
					filteredSubscriptionList.forEach(f -> {
						int days = f.getSubscriptionDuration().getDurationInDays();
						if(days == 30) response.setMonthlyAmount(f.getPrice());
						if(days == 180) response.setHalfYearlyAmount(f.getPrice());
						if(days == 365) response.setYearlyAmount(f.getPrice());
					});
				}
				plans.add(response);
			});
		}
		return plans;
	}

}
