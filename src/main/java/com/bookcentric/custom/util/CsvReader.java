package com.bookcentric.custom.util;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookcentric.component.books.BookService;
import com.bookcentric.component.books.Books;
import com.bookcentric.component.books.author.AuthorRepository;
import com.opencsv.CSVReader;

// @RestController
public class CsvReader {
	
	@Autowired BookService bookService; 
	
	//@GetMapping("/readcsv")
	public void readCsv() {
		try {
			Reader reader = Files.newBufferedReader(Paths.get("The Treasury.csv"));
			
			readAll(reader);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void readAll(Reader reader) throws Exception {
	    CSVReader csvReader = new CSVReader(reader);
	    List<String[]> list = new ArrayList<>();
	    String[] line;
	    while ((line = csvReader.readNext()) != null) {
	    	Books book = new Books();
	    	book.setCode(line[1]);
	    	book.setType(line[2]);
	    	book.setName(line[3]);
	    	book.setAuthor(null);
	    	book.setGenre(null);
	    	book.setPublisher(null);
	    	book.setGoodreadsLink(line[6]);
	    	System.out.println(book.toString());
	    	bookService.add(book);
	    	/*for(int i = 0; i < line.length; i++) {
	    		System.out.print(line[i]);
	    		System.out.print(" ");
	    	}
	    	System.out.println("***********");*/
	    	
	    }
	    reader.close();
	    csvReader.close();
	    
	    
	}
}
