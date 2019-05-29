package com.bookcentric.component.books;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mysql.jdbc.Blob;

@Service
public class BookServiceImpl implements BookService {
	
	@Autowired private BookRepository repository;

	@Override
	public Books getBy(Integer id) {
		return repository.getOne(id);
	}
	
	@Override
	public void add(Books book) {
		repository.save(book);
	}

	@Override
	public List<Books> getAll() {
		return repository.findAll();
	}

	@Override
	public List<BooksDTO> getFrom(List<Books> books) {
		List<BooksDTO> bookList = new ArrayList<>();
		ModelMapper mapper = new ModelMapper();
		
		books.forEach(b -> {
			BooksDTO booksDto = mapper.map(b, BooksDTO.class);
			bookList.add(booksDto);
		});
		return bookList;
	}

	@Override
	public List<BooksDTO> getFilterdHistoryByUnreturnedBooks(List<BooksDTO> bookList) {
		bookList.forEach(b -> {
			b.setUserHistory(b.getUserHistory().stream().filter(u -> u.getReturnDate() == null).collect(Collectors.toList()));
			updateCount(b);
		});
		return bookList;
	}
	
	private void updateCount(BooksDTO book) {
		book.setIssuedCount(book.getUserHistory().size());
		book.setRemainingCount(book.getCount() - book.getIssuedCount());
	}

	@Override
	public List<Books> getBestSellerBooks() {
		return repository.findByBestSeller(true);
	}

	@Override
	public List<Books> getNewArrivalBooks() {
		return repository.findByNewArrival(true);
	}

	@Override
	public void storeImage(MultipartFile file, Integer id) throws IOException {
		byte[] image = file.getBytes();
		repository.updateImageById(image, id);
		
	}

	@Override
	public byte[] getImageBy(Integer id) {
		return repository.getImageById(id);
	}

	@Override
	public void delete(Books book) {
		repository.delete(book);		
	}

}
