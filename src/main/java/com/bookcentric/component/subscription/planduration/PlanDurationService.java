package com.bookcentric.component.subscription.planduration;

import java.util.List;


public interface PlanDurationService {
	
	public PlanDuration findBy(Integer id);
	
	public void save(PlanDuration planDuration);
	
	public List<PlanDuration> findAll();
	
	public void delete(PlanDuration planDuration);
}
