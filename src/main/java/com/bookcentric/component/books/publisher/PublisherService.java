package com.bookcentric.component.books.publisher;

import java.util.List;

public interface PublisherService {
	
	public Publisher findBy(Integer id);
	
	public void save(Publisher publisher);
	
	public List<Publisher> findAll();
	
	public void delete(Publisher publisher);
}
