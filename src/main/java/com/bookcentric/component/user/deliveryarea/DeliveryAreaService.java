package com.bookcentric.component.user.deliveryarea;

import java.util.List;

public interface DeliveryAreaService {
	public List<DeliveryArea> findAll();
	
	public DeliveryArea findBy(Integer id);
	
	public void save(DeliveryArea borrowLimit);
	
	public void delete(DeliveryArea borrowLimit);

}
