package com.bookcentric.component.books.publisher;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublisherServiceImpl implements PublisherService{
	
	@Autowired PublisherRepository repository;

	@Override
	public List<Publisher> getAll() {

		return repository.findAll();
	}

}
