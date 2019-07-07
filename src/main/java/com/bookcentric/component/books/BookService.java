package com.bookcentric.component.books;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface BookService {
	public Books getBy(Integer id);
	
	public void add(Books book);
	
	public List<Books> getAll();
	
	public List<BooksDTO> getFrom(List<Books> books);
	
	public List<BooksDTO> getFilterdHistoryByUnreturnedBooks(List<BooksDTO> books);
	
	public List<Books> getBestSellerBooks();
	
	public List<Books> getNewArrivalBooks();
	
	public void storeImage(MultipartFile file, Integer id) throws IOException;
	
	public byte[] getImageBy(Integer id);
	
	public void delete(Books book);
	
	public List<Books> searchByBookName(String searchText);
	
	public void updateCount(BooksDTO book);
}
