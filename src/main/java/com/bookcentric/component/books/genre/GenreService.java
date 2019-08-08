package com.bookcentric.component.books.genre;

import java.util.List;

public interface GenreService {
	public Genre findBy(Integer id);
	
	public void save(Genre genre);
	
	public List<Genre> findAll();
	
	public void delete(Genre genre);

}
