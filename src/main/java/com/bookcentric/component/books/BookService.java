package com.bookcentric.component.books;

import java.util.List;

public interface BookService {
	public Books getBy(Integer id);
	
	public void add(Books book);
	
	public List<Books> getAll();
	
	public List<BooksDTO> getFrom(List<Books> books);
	
	public List<BooksDTO> getFilterdHistoryByUnreturnedBooks(List<BooksDTO> books);
}
