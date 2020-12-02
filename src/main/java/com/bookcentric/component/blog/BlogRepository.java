package com.bookcentric.component.blog;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

public interface BlogRepository extends JpaRepository<Blog, Integer> {
	
	@Query(value = "Update blog set image = :image Where id=:id", nativeQuery=true) @Async @Modifying @Transactional
	public void updateImageById(@Param("image") byte[] image, @Param("id") Integer id);
	
	@Query(value = "Select image From blog where id = :id", nativeQuery=true)
	public byte[] getImageById(@Param("id") Integer id);
	
	@Query(value = "Select * From blog where published=true Order by id desc", nativeQuery=true)
	public List<Blog> findAllByPublished();
	
	@Query(value = "Select * From blog where userId = :id Order by id desc", nativeQuery=true)
	public List<Blog> findAllById(@Param("id") Integer id);
}
