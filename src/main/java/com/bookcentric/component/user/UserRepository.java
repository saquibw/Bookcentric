package com.bookcentric.component.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	@Query(value = "Select u FROM User u WHERE u.status.id = 1 AND DATEDIFF(dateOfRenewal, NOW()) = :expiryAfter")
	public List<User> findByActiveUserAndSubscriptionExpiry(@Param("expiryAfter") Integer expiryAfter);
}
