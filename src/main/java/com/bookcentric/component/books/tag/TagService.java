package com.bookcentric.component.books.tag;

import java.util.List;

public interface TagService {
	
	public Tag findBy(Integer id);
	
	public void save(Tag tag);
	
	public List<Tag> findAll();
	
	public void delete(Tag tag);
}
