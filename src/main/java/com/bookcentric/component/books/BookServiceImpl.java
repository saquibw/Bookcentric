package com.bookcentric.component.books;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {
	
	@Autowired private BookRepository repository;

	@Override
	public Books getBy(Integer id) {
		return repository.getOne(id);
	}

}
