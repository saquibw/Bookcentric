package com.bookcentric.component.user.testimonial;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestimonialServiceImpl implements TestimonialService {
	
	@Autowired TestimonialRepository repo;

	@Override
	public List<Testimonial> findAll() {
		return repo.findAll();
	}

	@Override
	public Testimonial findBy(int id) {
		return repo.getOne(id);
	}

	@Override
	public void save(Testimonial testimonial) {
		repo.save(testimonial);
	}

	@Override
	public void delete(Testimonial testimonial) {
		repo.delete(testimonial);
	}

	@Override
	public void deleteBy(Integer id) {
		repo.deleteById(id);
	}

}
