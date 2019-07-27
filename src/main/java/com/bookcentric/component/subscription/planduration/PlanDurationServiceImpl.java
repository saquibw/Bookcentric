package com.bookcentric.component.subscription.planduration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlanDurationServiceImpl implements PlanDurationService {
	
	@Autowired PlanDurationRepository repository;

	@Override
	public PlanDuration findBy(Integer id) {
		return repository.getOne(id);
	}

	@Override
	public void save(PlanDuration planDuration) {
		repository.save(planDuration);
	}

	@Override
	public List<PlanDuration> findAll() {
		return repository.findAll();
	}

	@Override
	public void delete(PlanDuration planDuration) {
		repository.delete(planDuration);
	}

}
