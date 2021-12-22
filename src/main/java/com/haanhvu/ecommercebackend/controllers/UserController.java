package com.haanhvu.ecommercebackend.controllers;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.haanhvu.ecommercebackend.model.persistence.Cart;
import com.haanhvu.ecommercebackend.model.persistence.User;
import com.haanhvu.ecommercebackend.model.persistence.repositories.CartRepository;
import com.haanhvu.ecommercebackend.model.persistence.repositories.UserRepository;
import com.haanhvu.ecommercebackend.model.requests.CreateUserRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		User user = new User();
		user.setUsername(createUserRequest.getUsername());
		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);
		
		if(createUserRequest.getPassword().length()<7 ||
				!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())){
			return ResponseEntity.badRequest().build();
		}
		
		SecureRandom secureRandom = new SecureRandom();
		byte[] randomBytes = new byte[16];
		secureRandom.nextBytes(randomBytes);
		String salt = Base64.getEncoder().encodeToString(randomBytes);
		user.setSalt(salt);
		
		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword() + user.getSalt()));
		userRepository.save(user);
		return ResponseEntity.ok(user);
	}
	
}
