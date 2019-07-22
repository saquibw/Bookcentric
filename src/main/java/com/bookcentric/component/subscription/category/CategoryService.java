package com.bookcentric.component.subscription.category;

import java.util.List;

public interface CategoryService {
	
	public Category findBy(Integer id);
	
	public void save(Category category);
	
	public List<Category> findAll();
	
	public void delete(Category category);
}
