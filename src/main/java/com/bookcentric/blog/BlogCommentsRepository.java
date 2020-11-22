package com.bookcentric.blog;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogCommentsRepository extends JpaRepository<BlogComments, Integer> {
	public BlogComments findOneByBlogIdAndUserId(Integer blogId, Integer userId);
	public List<BlogComments> findAllByBlogIdOrderByCreatedAtDesc(Integer id);
}
