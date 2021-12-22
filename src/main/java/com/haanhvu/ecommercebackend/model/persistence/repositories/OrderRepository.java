package com.haanhvu.ecommercebackend.model.persistence.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.haanhvu.ecommercebackend.model.persistence.User;
import com.haanhvu.ecommercebackend.model.persistence.UserOrder;

public interface OrderRepository extends JpaRepository<UserOrder, Long> {
	List<UserOrder> findByUser(User user);
}
