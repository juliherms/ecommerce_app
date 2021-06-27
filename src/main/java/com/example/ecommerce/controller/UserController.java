package com.example.ecommerce.controller;

import com.example.ecommerce.dto.CreateUserRequestDTO;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repositories.CartRepository;
import com.example.ecommerce.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * This class responsible to provide access from user
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    /**
     * Return user from id
     * @param id
     * @return
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return ResponseEntity.of(userRepository.findById(id));
    }

    /**
     * Return user from username
     * @param username
     * @return
     */
    @GetMapping("/{username}")
    public ResponseEntity<User> findByUserName(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
    }

    /**
     * Create user
     * @param createUserRequest
     * @return
     */
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequestDTO createUserRequest) {

        User user = new User();
        user.setUsername(createUserRequest.getUsername());

        Cart cart = new Cart();

        cartRepository.save(cart);
        user.setCart(cart);

        if(createUserRequest.getPassword().length() < 7 ){
            log.error("[createUser] [Error] for user : " + user.getUsername() +", REASON : invalid password" );
            return ResponseEntity.badRequest().body("Password must be at least 7 characters.");
        }else if (!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())){
            log.error("[createUser] [Error] for user : " + user.getUsername() +", REASON : password mismatching" );
            return ResponseEntity.badRequest().body("Password field does not match confirm password field");
        }

        user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));

        userRepository.save(user);

        log.info("[createUser] [Success] for user : " + user.getUsername());

        return ResponseEntity.ok(user);
    }
}
