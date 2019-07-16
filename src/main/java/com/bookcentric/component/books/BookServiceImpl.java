package com.bookcentric.component.books;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.cfg.SerializerFactoryConfig;

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
	
	public void updateCount(BooksDTO book) {
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

	@Override
	public List<Books> searchByBookName(String searchText) {
		List<Books> books = repository.findAll();
		
		return filterBy(searchText.toLowerCase(), books);
	}
	
	public Page<Books> getAllByPageAndSort(Integer initialCount, Integer totalLimit, String searchText) {
		Sort sort = Sort.by(Sort.Direction.ASC, "name");
		Pageable pageable =  PageRequest.of(initialCount, totalLimit, sort);
		
		Page<Books> books = repository.findAll(pageable);

		if(!searchText.isEmpty()) {
			String search = searchText.toLowerCase();
			
			List<Books> bookList = filterBy(search, books);

			books = new PageImpl<>(bookList, pageable, bookList.size());
		}		
		
		return books;
	}
	
	private List<Books> filterBy(String searchText, Page<Books> list) {
		return list
				.stream()
				.filter(b -> b.getName().toLowerCase().contains(searchText) || b.getAuthor().getName().toLowerCase().contains(searchText) || b.getGenre().getName().toLowerCase().contains(searchText))
				.collect(Collectors.toList());
	}
	
	private List<Books> filterBy(String searchText, List<Books> list) {
		System.out.println(list.size());
		return list
				.stream()
				.filter(b -> b.getName().toLowerCase().contains(searchText) || b.getAuthor().getName().toLowerCase().contains(searchText) || b.getGenre().getName().toLowerCase().contains(searchText))
				.collect(Collectors.toList());
	}

}
