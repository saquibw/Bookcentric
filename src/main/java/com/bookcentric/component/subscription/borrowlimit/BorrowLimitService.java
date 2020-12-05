package com.bookcentric.component.subscription.borrowlimit;

import java.util.List;

public interface BorrowLimitService {

	public BorrowLimit findBy(Integer id);
	
	public void save(BorrowLimit borrowLimit);
	
	public List<BorrowLimit> findAll();
	
	public void delete(BorrowLimit borrowLimit);
	
	public void deleteBy(Integer id);
}
