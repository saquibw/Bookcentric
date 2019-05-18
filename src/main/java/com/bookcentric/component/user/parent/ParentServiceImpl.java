package com.bookcentric.component.user.parent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParentServiceImpl implements ParentService {
	
	@Autowired ParentRepository repo;

	@Override
	public void save(Parent parent) {
		repo.save(parent);

	}

}
