package com.bookcentric.component.books.tag;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl implements TagService {
	@Autowired TagRepository repository;

	@Override
	public Tag findBy(Integer id) {
		return repository.getOne(id);
	}

	@Override
	public void save(Tag tag) {
		repository.save(tag);
	}

	@Override
	public List<Tag> findAll() {
		return repository.findAll();
	}

	@Override
	public void delete(Tag tag) {
		repository.delete(tag);
	}

	@Override
	public void deleteBy(Integer id) {
		repository.deleteById(id);
	}

}
