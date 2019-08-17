package com.bookcentric.component.books;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

public interface BookRepository extends JpaRepository<Books, Integer> {
	public List<Books> findByBestSeller(boolean status);
	public List<Books> findByNewArrival(boolean status);
	public List<Books> findByChildren(boolean status);
	public List<Books> findByReadingChallenge(boolean status);
	
	@Query(value = "Update books set image = :image Where id=:id", nativeQuery=true) @Async @Modifying @Transactional
	public void updateImageById(@Param("image") byte[] image, @Param("id") Integer id);
	
	@Query(value = "Select image From books where id = :id", nativeQuery=true)
	public byte[] getImageById(@Param("id") Integer id);
	
	/*@Query(value = "Select * From books where name like CONCAT('%', :searchText, '%')", nativeQuery=true)
	public List<Books> getByBookName(@Param("searchText") String searchText);*/
}
