package com.bookcentric.events;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

public interface EventRepository extends JpaRepository<Event, Integer> {
	
	@Query(value = "Update event set image = :image Where id=:id", nativeQuery=true) @Async @Modifying @Transactional
	public void updateImageById(@Param("image") byte[] image, @Param("id") Integer id);
	
	@Query(value = "Select image From event where id = :id", nativeQuery=true)
	public byte[] getImageById(@Param("id") Integer id);
	
	public List<Event> findAllByOrderByCreatedAtDesc();

}
