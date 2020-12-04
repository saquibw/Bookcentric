package com.bookcentric.component.books.author;

import java.util.List;

public interface AuthorService {
	
	public Author findBy(Integer id);
	
	public void save(Author author);
	
	public List<Author> findAll();
	
	public void delete(Author author);
	
	public void deleteBy(Integer id);
}
