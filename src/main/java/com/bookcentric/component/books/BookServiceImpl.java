package com.bookcentric.component.books;

import java.io.IOException;
import java.util.ArrayList;
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
	
	@Override
	public Page<Books> getAllByPageAndSort(Integer pageNumber, Integer pageSize, String searchText) {
		Sort sort = Sort.by(Sort.Direction.ASC, "name");
		Pageable pageable =  PageRequest.of(pageNumber, pageSize, sort);
		
		Page<Books> books = null;

		if(!searchText.isEmpty()) {
			String search = searchText.toLowerCase();
			
			List<Books> bookList = filterBy(search, repository.findAll());

			books = new PageImpl<>(bookList, pageable, bookList.size());
		} else {
			books = repository.findAll(pageable);
		}	

		return books;
	}
	
	/*private List<Books> filterBy(String searchText, Page<Books> list) {
		return list
				.stream()
				.filter(b -> b.getName().toLowerCase().contains(searchText) || b.getAuthorName().toLowerCase().contains(searchText) || b.getGenreName().toLowerCase().contains(searchText))
				.collect(Collectors.toList());
	}*/
	
	private List<Books> filterBy(String searchText, List<Books> list) {
		return list
				.stream()
				.filter(b -> b.getName().toLowerCase().contains(searchText) 
						|| b.getAuthorName().toLowerCase().contains(searchText) 
						|| b.getGenreName().toLowerCase().contains(searchText) 
						/*|| b.getPublisherName().toLowerCase().contains(searchText)*/)
				.collect(Collectors.toList());
	}

}
