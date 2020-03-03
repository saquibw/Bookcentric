package com.bookcentric.component.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	@Query(value = "Select u FROM User u WHERE u.status.id = 1 AND DATEDIFF(dateOfRenewal, NOW()) = :expiryAfter")
	public List<User> findByActiveUserAndSubscriptionExpiry(@Param("expiryAfter") Integer expiryAfter);
	
	@Query(value = "Select u FROM User u WHERE u.status.id = 1 AND u.dateOfRenewal = CURDATE()")
	public List<User> findAllActiveExpiresToday();
	
	public User getByEmail(String email);
	
	public User findByMembershipId(String id);
	
	@Query(value = "Update user set image = :image Where id=:id", nativeQuery=true) @Async @Modifying @Transactional
	public void updateImageById(@Param("image") byte[] image, @Param("id") Integer id);
	
	@Query(value = "Select image From user where id = :id", nativeQuery=true)
	public byte[] getImageById(@Param("id") Integer id);
}
