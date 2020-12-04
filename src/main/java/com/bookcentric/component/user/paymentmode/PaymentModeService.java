package com.bookcentric.component.user.paymentmode;

import java.util.List;

public interface PaymentModeService {
	
	public List<PaymentMode> findAll();
	
	public PaymentMode findBy(Integer id);
	
	public void save(PaymentMode paymentMode);
	
	public void delete(PaymentMode paymentMode);
	
	public void deleteBy(Integer id);
}
