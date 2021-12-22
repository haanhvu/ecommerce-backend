package com.haanhvu.ecommercebackend.model.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.haanhvu.ecommercebackend.model.persistence.Cart;
import com.haanhvu.ecommercebackend.model.persistence.User;

public interface CartRepository extends JpaRepository<Cart, Long> {
	Cart findByUser(User user);
}
