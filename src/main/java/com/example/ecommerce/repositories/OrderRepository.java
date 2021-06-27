package com.example.ecommerce.repositories;

import com.example.ecommerce.model.User;
import com.example.ecommerce.model.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<UserOrder, Long> {

    /**
     * list orders by user
     * @param user
     * @return
     */
    List<UserOrder> findByUser(User user);
}
