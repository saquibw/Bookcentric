package com.bookcentric.component.books.genre;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenreServiceImpl implements GenreService {
	
	@Autowired GenreRepository repository;

	@Override
	public List<Genre> findAll() {
		return repository.findAll();
	}

	@Override
	public Genre findBy(Integer id) {
		return repository.getOne(id);
	}

	@Override
	public void save(Genre genre) {
		repository.save(genre);
	}

	@Override
	public void delete(Genre genre) {
		repository.delete(genre);
	}

	@Override
	public void deleteBy(Integer id) {
		repository.deleteById(id);		
	}

}
