package com.example.ecommerce.controller;

import com.example.ecommerce.model.User;
import com.example.ecommerce.model.UserOrder;
import com.example.ecommerce.repositories.OrderRepository;
import com.example.ecommerce.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Class responsible to provide resource from order
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    private final Logger log = LoggerFactory.getLogger(OrderController.class);

    @PostMapping("/submit/{username}")
    public ResponseEntity<UserOrder> submit(@PathVariable String username) {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            log.error("[submit] [Error] for user : " + username +", REASON : User not found" );
            return ResponseEntity.notFound().build();
        }
        UserOrder order = UserOrder.cartToOrderConverter(user.getCart());
        orderRepository.save(order);
        log.info("Order submitted successfully.");
        return ResponseEntity.ok(order);
    }

    /**
     * List order history from username
     * @param username
     * @return
     */
    @GetMapping("/history/{username}")
    public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {

        User user = userRepository.findByUsername(username);

        if(user == null) {
            log.error("[getOrdersForUser] [Error] for user : " + username +", REASON : User not found" );
            return ResponseEntity.notFound().build();
        }

        List<UserOrder> history = orderRepository.findByUser(user);

        log.info("[getOrdersForUser] [Success] for user : " + user.getUsername());
        return ResponseEntity.ok(history);
    }
}