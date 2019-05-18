package com.bookcentric.component.user.deliveryarea;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryAreaServiceImpl implements DeliveryAreaService {
	
	@Autowired DeliveryAreaRepository repo;

	@Override
	public List<DeliveryArea> findAll() {
		
		return repo.findAll();
	}

}
