package com.haanhvu.ecommercebackend.controllers;

import com.haanhvu.ecommercebackend.TestUtils;
import com.haanhvu.ecommercebackend.controllers.CartController;
import com.haanhvu.ecommercebackend.model.persistence.Cart;
import com.haanhvu.ecommercebackend.model.persistence.Item;
import com.haanhvu.ecommercebackend.model.persistence.User;
import com.haanhvu.ecommercebackend.model.persistence.repositories.CartRepository;
import com.haanhvu.ecommercebackend.model.persistence.repositories.ItemRepository;
import com.haanhvu.ecommercebackend.model.persistence.repositories.UserRepository;
import com.haanhvu.ecommercebackend.model.requests.ModifyCartRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class CartControllerTest {
	
    private CartController cartController;

    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private ItemRepository itemRepo = mock(ItemRepository.class);
    
    private Cart cart = new Cart();
    private User user = new User();
    private Item item = new Item();

    @Before
    public void setup(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepo);
        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("testpassword");
        user.setCart(cart);
        when(userRepo.findByUsername("testuser")).thenReturn(user);
        
        item.setId(0L);
        item.setName("oreo cake");
        item.setPrice(BigDecimal.valueOf(49.99));    
        when(itemRepo.findById(0L)).thenReturn(Optional.of(item));
    }


    @Test
    public void addToCart() {
        ModifyCartRequest request = new ModifyCartRequest();

        request.setItemId(0L);
        request.setQuantity(1);
        request.setUsername(user.getUsername());

        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(BigDecimal.valueOf(49.99), response.getBody().getTotal());
        assertEquals(1, response.getBody().getItems().size());
    }

    @Test
    public void removeFromCart() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(0L);
        request.setQuantity(2);
        request.setUsername(user.getUsername());
        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(BigDecimal.valueOf(49.99*2), response.getBody().getTotal());
        assertEquals(2, response.getBody().getItems().size());

        ModifyCartRequest modifiedRequest = new ModifyCartRequest();
        modifiedRequest.setItemId(0L);
        modifiedRequest.setQuantity(1);
        modifiedRequest.setUsername(user.getUsername());
        response = cartController.removeFromcart(modifiedRequest);
        assertEquals(BigDecimal.valueOf(49.99), response.getBody().getTotal());
        assertEquals(1, response.getBody().getItems().size());
    }

}