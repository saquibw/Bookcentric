package com.bookcentric.component.books.genre;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenreServiceImpl implements GenreService {
	
	@Autowired GenreRepository repository;

	@Override
	public List<Genre> getAll() {
		return repository.findAll();
	}

}
