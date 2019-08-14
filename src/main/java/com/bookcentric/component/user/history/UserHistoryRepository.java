package com.bookcentric.component.user.history;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserHistoryRepository extends JpaRepository<UserHistory, Integer> {
	
	@Query(value = "Select h FROM UserHistory h "
			+ "WHERE DATEDIFF(h.dueDate, now()) = :expiryAfter "
			+ "And h.returnDate IS NULL")
	public List<UserHistory> findByActiveUserAndPlanExpiry(@Param("expiryAfter") Integer expiryAfter);
}
