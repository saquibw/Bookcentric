package com.bookcentric.component.books.author;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorServiceImpl implements AuthorService {
	
	@Autowired AuthorRepository repository;

	@Override
	public List<Author> getAll() {
		
		return repository.findAll();
	}

}
