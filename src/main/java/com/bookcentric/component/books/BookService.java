package com.bookcentric.component.books;

public interface BookService {
	public Books getBy(Integer id);
	
	public void add(Books book);
}
