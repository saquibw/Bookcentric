package com.bookcentric.component.readingchallenge;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

public interface ReadingChallengeRepository extends JpaRepository<ReadingChallenge, Integer> {
	
	@Query(value = "Update reading_challenge set image = :image Where id=:id", nativeQuery=true) @Async @Modifying @Transactional
	public void updateImageById(@Param("image") byte[] image, @Param("id") Integer id);
	
	@Query(value = "Select image From reading_challenge where id = :id", nativeQuery=true)
	public byte[] getImageById(@Param("id") Integer id);
	
	
	public List<ReadingChallenge> findAllByOrderByIdDesc();
	
	@Query(value = "Select * From reading_challenge where published=true Order by id desc", nativeQuery=true)
	public List<ReadingChallenge> findAllByPublished();
	
	@Query(value = "Select * From reading_challenge where published=true Order by id desc limit 1", nativeQuery=true)
	public ReadingChallenge getLatest();
}
