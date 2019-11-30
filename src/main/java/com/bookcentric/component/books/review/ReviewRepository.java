package com.bookcentric.component.books.review;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
	public Review findOneByBookIdAndUserId(Integer bookId, Integer userId);
	public List<Review> findAllByBookId(Integer id);
}
