package com.bookcentric.component.books.author;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorServiceImpl implements AuthorService {
	
	@Autowired AuthorRepository repository;

	@Override
	public List<Author> findAll() {
		
		return repository.findAll();
	}

	@Override
	public Author findBy(Integer id) {
		return repository.getOne(id);
	}

	@Override
	public void save(Author author) {
		repository.save(author);
		
	}

	@Override
	public void delete(Author author) {
		repository.delete(author);
		
	}

}
