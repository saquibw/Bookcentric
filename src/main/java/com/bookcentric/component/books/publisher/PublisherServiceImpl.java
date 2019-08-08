package com.bookcentric.component.books.publisher;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublisherServiceImpl implements PublisherService{
	
	@Autowired PublisherRepository repository;

	@Override
	public List<Publisher> findAll() {

		return repository.findAll();
	}

	@Override
	public Publisher findBy(Integer id) {
		return repository.getOne(id);
	}

	@Override
	public void save(Publisher publisher) {
		repository.save(publisher);
	}

	@Override
	public void delete(Publisher publisher) {
		repository.delete(publisher);
	}

}
