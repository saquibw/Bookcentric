package com.bookcentric.component.subscription.borrowlimit;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BorrowLimitServiceImpl implements BorrowLimitService {
	
	@Autowired BorrowLimitRepository repository;

	@Override
	public BorrowLimit findBy(Integer id) {
		return repository.getOne(id);
	}

	@Override
	public void save(BorrowLimit borrowLimit) {
		repository.save(borrowLimit);

	}

	@Override
	public List<BorrowLimit> findAll() {
		return repository.findAll();
	}

	@Override
	public void delete(BorrowLimit borrowLimit) {
		repository.delete(borrowLimit);
	}

}
