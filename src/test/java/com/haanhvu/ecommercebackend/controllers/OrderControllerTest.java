package com.haanhvu.ecommercebackend.controllers;

import com.haanhvu.ecommercebackend.TestUtils;
import com.haanhvu.ecommercebackend.controllers.OrderController;
import com.haanhvu.ecommercebackend.model.persistence.Cart;
import com.haanhvu.ecommercebackend.model.persistence.Item;
import com.haanhvu.ecommercebackend.model.persistence.User;
import com.haanhvu.ecommercebackend.model.persistence.UserOrder;
import com.haanhvu.ecommercebackend.model.persistence.repositories.OrderRepository;
import com.haanhvu.ecommercebackend.model.persistence.repositories.UserRepository;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
	
    private OrderController orderController;
    private OrderRepository orderRepo = mock(OrderRepository.class);
    private UserRepository userRepo = mock(UserRepository.class);

    private User user = new User();
    private Item item = new Item();
    private Cart cart = new Cart();
    
    private List<UserOrder> userOrderList = new ArrayList();
    private UserOrder userOrder = new UserOrder();
    private List<Item> itemsList = new ArrayList<Item>();

    @Before
    public void setup(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);
        TestUtils.injectObjects(orderController, "userRepository", userRepo);

        user.setId(0L);
        user.setUsername("testusername");
        user.setPassword("testpassword");
        when(userRepo.findByUsername("testusername")).thenReturn(user);

        item.setId(0L);
        item.setPrice(BigDecimal.valueOf(49.99));
        item.setName("oreo_cake");
        item.setDescription("This is oreo cake description.");
        itemsList.add(item);
        
        cart.setId(0L);
        cart.setItems(itemsList);
        cart.setUser(user);
        cart.setTotal(BigDecimal.valueOf(49.99));
        user.setCart(cart);
        
        userOrder.setItems(itemsList);
        userOrderList.add(userOrder);
        when(orderRepo.findByUser(user)).thenReturn(userOrderList);
    }

    @Test
    public void testSubmitOrder(){
        ResponseEntity<UserOrder> response = orderController.submit("testusername");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(BigDecimal.valueOf(49.99), response.getBody().getTotal());
        assertEquals(1, response.getBody().getItems().size());
    }
    
    @Test
    public void testGetOrdersForUser(){
    	ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("testusername");
    	List<UserOrder> userOrderList = response.getBody();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userOrderList.size(), 1);
        assertEquals(userOrderList.get(0), userOrder);
    }

}