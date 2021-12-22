package com.haanhvu.ecommercebackend.controllers;

import com.haanhvu.ecommercebackend.TestUtils;
import com.haanhvu.ecommercebackend.controllers.UserController;
import com.haanhvu.ecommercebackend.model.persistence.User;
import com.haanhvu.ecommercebackend.model.persistence.repositories.CartRepository;
import com.haanhvu.ecommercebackend.model.persistence.repositories.UserRepository;
import com.haanhvu.ecommercebackend.model.requests.CreateUserRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;

public class UserControllerTest {
    
	private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void testCreateUser() { 
    	CreateUserRequest request = new CreateUserRequest();
    	when(encoder.encode(any(String.class))).thenReturn("thisIsHashed");    
        request.setUsername("testUser");
        request.setPassword("testPassword");
        request.setConfirmPassword("testPassword");
        ResponseEntity<User> response = userController.createUser(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0 , user.getId());
        assertEquals("testUser", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());
    }
    
}    