package com.bookcentric.component.subscription.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired CategoryRepository repository;

	@Override
	public Category findBy(Integer id) {
		return repository.getOne(id);
	}

	@Override
	public void save(Category category) {
		repository.save(category);
		
	}

	@Override
	public List<Category> findAll() {
		return repository.findAll();
	}

	@Override
	public void delete(Category category) {
		repository.delete(category);
		
	}

}
