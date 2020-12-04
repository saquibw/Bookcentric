package com.bookcentric.component.user.paymentmode;

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
public class PaymentModeController {
	
	@Autowired PaymentModeService paymentModeaService;
	
	@GetMapping({"/paymentmode/view", "/paymentmode/edit/{id}"})
	public ModelAndView viewPaymentModePage(@PathVariable(name="id", required=false) Integer id) {
		ModelAndView view = new ModelAndView("payment-mode");
		
		PaymentMode paymentMode = new PaymentMode();
		
		if(id != null && id > 0) {
			paymentMode = paymentModeaService.findBy(id);
		}
		List<PaymentMode> paymentModeList = paymentModeaService.findAll();
		
		view.addObject("paymentMode", paymentMode);
		view.addObject("paymentModeList", paymentModeList);
		view.addObject("pageTitle", "BookCentric - Payment Mode");
		
		return view;
	}
	
	@PostMapping("/paymentmode/add")
	public String savePaymentMode(PaymentMode paymentMode) {
		paymentModeaService.save(paymentMode);
		
		return "redirect:/paymentmode/view";
	}
	
	@ResponseBody
	@PostMapping("/paymentmode/delete")
	public Response deletePaymentMode(@RequestParam("id") Integer id) {
		paymentModeaService.deleteBy(id);
		
		Response response = new Response();
		response.setSuccess(true);

		return response;
	}
}
