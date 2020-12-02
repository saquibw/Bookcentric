package com.bookcentric.component.events;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.bookcentric.component.user.security.UserSecurityService;

@Controller
public class EventController {
	
	@Autowired EventRepository repository;
	@Autowired UserSecurityService userSecurityService;
	@Autowired HttpServletResponse response;

	@GetMapping({"/events/me", "/events/me/{id}"})
	public ModelAndView eventView(@PathVariable(name="id", required=false) Integer id) {
		ModelAndView view = new ModelAndView("event");
		Event event = new Event();
		
		if(id != null && id > 0) {
			event = repository.getOne(id);
		}
		view.addObject("event", event);
		view.addObject("pageTitle", "BookCentric - Event");
		
		return view;
	}
	
	@PostMapping("/events/me")
	public String saveEvent(Event event, @RequestParam("file") MultipartFile file) {	
		if(!ObjectUtils.isEmpty(event.getId())) {
			Event e = repository.getOne(event.getId());
			event.setCreatedAt(e.getCreatedAt());
		}
		repository.save(event);

		if(file != null && file.getSize() > 0) {			
			try {
				byte[] image = file.getBytes();
				repository.updateImageById(image, event.getId());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "redirect:/events/all";
	}
	
	@GetMapping("/events/me/delete/{id}")
	public String deleteEvent(@PathVariable(name="id") Integer id) {
		repository.deleteById(id);
		return "redirect:/events/all";
	}
	
	@GetMapping("/events/all")
	public ModelAndView showEventList() {
		ModelAndView view = new ModelAndView("event-list");
		view.addObject("events", repository.findAllByOrderByCreatedAtDesc());
		view.addObject("pageTitle", "BookCentric - Event");
		
		return view;
	}
	
	@GetMapping("/events/all/{id}")
	public ModelAndView showEvent(@PathVariable(name="id") Integer id) {
		ModelAndView view = new ModelAndView("event-view");
		Event event = repository.getOne(id);
		
		view.addObject("event", event);
		view.addObject("pageTitle", "BookCentric - Event");
		
		return view;
	}
	
	@GetMapping("/events/all/image/{id}")
	@ResponseBody
	public void getImage(@PathVariable Integer id) throws SQLException, IOException {
		response.setContentType("image/jpeg");

		byte[] image = repository.getImageById(id);
		if(image != null) {

			ServletOutputStream stream = response.getOutputStream();
			stream.write(image);
			stream.flush();
			stream.close();
		}		
	}
}
