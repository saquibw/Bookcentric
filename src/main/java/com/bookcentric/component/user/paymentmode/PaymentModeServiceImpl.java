package com.bookcentric.component.user.paymentmode;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentModeServiceImpl implements PaymentModeService {
	
	@Autowired PaymentModeRepository repo;

	@Override
	public List<PaymentMode> findAll() {
		return repo.findAll();
	}
	
	@Override
	public PaymentMode findBy(Integer id) {
		return repo.getOne(id);
	}

	@Override
	public void save(PaymentMode paymentMode) {
		repo.save(paymentMode);
	}
	
	@Override
	public void delete(PaymentMode paymentMode) {
		repo.delete(paymentMode);
	}

}
