package com.example.ecommerce.repositories;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    /**
     * find cart by user
     * @param user
     * @return
     */
    Cart findByUser(User user);
}
