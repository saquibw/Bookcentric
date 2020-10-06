package com.bookcentric.component.user.testimonial;

import java.util.List;

public interface TestimonialService {
	
	public List<Testimonial> findAll();
	
	public Testimonial findBy(int id);
	
	public void save(Testimonial testimonial);
	
	public void delete(Testimonial testimonial);
}
