package com.example.ecommerce.controller;

import com.example.ecommerce.dto.ModifyCartRequestDTO;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.Item;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repositories.CartRepository;
import com.example.ecommerce.repositories.ItemRepository;
import com.example.ecommerce.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ItemRepository itemRepository;

    private final Logger log = LoggerFactory.getLogger(CartController.class);

    /**
     * Responsible to add item to cart
     * @param request
     * @return
     */
    @PostMapping("/addToCart")
    public ResponseEntity<Cart> addToCart(@RequestBody ModifyCartRequestDTO request) {
        return processCartRequest(request, false);
    }

    /**
     * Responsible to remove item to cart
     * @param request
     * @return
     */
    @PostMapping("/removeFromCart")
    public ResponseEntity<Cart> removeFromCart(@RequestBody ModifyCartRequestDTO request) {
        return processCartRequest(request, true);
    }

    /**
     * Method responsible to process item to cart
     * @param request
     * @param isRemoving
     * @return
     */
    private ResponseEntity<Cart> processCartRequest(ModifyCartRequestDTO request, Boolean isRemoving) {

        User user = userRepository.findByUsername(request.getUsername());

        if(user == null) {
            log.error("[processCartRequest] [Error] for user : " + request.getUsername() +", REASON : User not found" );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Optional<Item> item = itemRepository.findById(request.getItemId());

        if(!item.isPresent()) {
            log.error("[processCartRequest] [Error] for item : " + request.getItemId() +", REASON : Item not found" );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Cart cart = user.getCart();
        if (isRemoving) {
            IntStream.range(0, request.getQuantity())
                    .forEach(i -> cart.removeItem(item.get()));
            log.info("[processCartRequest] [Success] for user : " + user.getUsername());
        } else {
            IntStream.range(0, request.getQuantity())
                    .forEach(i -> cart.addItem(item.get()));
            log.info("[processCartRequest] [Success] for user : " + user.getUsername());
        }
        cartRepository.save(cart);

        return ResponseEntity.ok(cart);
    }
}
