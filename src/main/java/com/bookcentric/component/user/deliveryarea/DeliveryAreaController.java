package com.bookcentric.component.user.deliveryarea;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bookcentric.custom.util.Response;

@Controller
public class DeliveryAreaController {

	@Autowired DeliveryAreaService deliveryAreaService;
	
	@GetMapping({"/deliveryarea/view", "/deliveryarea/edit/{id}"})
	public ModelAndView viewDeliveryAreaPage(@PathVariable(name="id", required=false) Integer id) {
		ModelAndView view = new ModelAndView("delivery-area");
		
		DeliveryArea deliveryArea = new DeliveryArea();
		
		if(id != null && id > 0) {
			deliveryArea = deliveryAreaService.findBy(id);
		}
		List<DeliveryArea> deliveryAreaList = deliveryAreaService.findAll();
		
		view.addObject("deliveryArea", deliveryArea);
		view.addObject("deliveryAreaList", deliveryAreaList);
		view.addObject("pageTitle", "BookCentric - Delivery Area");
		
		return view;
	}
	
	@PostMapping("/deliveryarea/add")
	public String saveDeliveryArea(DeliveryArea deliveryArea) {
		deliveryAreaService.save(deliveryArea);
		
		return "redirect:/deliveryarea/view";
	}
	
	@ResponseBody
	@PostMapping("/deliveryarea/delete")
	public Response deleteDeliveryArea(@RequestParam("id") Integer id) {
		deliveryAreaService.deleteBy(id);
		
		Response response = new Response();
		response.setSuccess(true);

		return response;
	}
}
