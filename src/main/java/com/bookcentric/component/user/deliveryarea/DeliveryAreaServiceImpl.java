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
	
	@Override
	public DeliveryArea findBy(Integer id) {
		return repo.getOne(id);
	}

	@Override
	public void save(DeliveryArea deliveryArea) {
		repo.save(deliveryArea);
	}
	
	@Override
	public void delete(DeliveryArea deliveryArea) {
		repo.delete(deliveryArea);
	}

	@Override
	public void deleteBy(Integer id) {
		repo.deleteById(id);
	}

}
