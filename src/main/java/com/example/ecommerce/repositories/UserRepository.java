package com.example.ecommerce.repositories;

import com.example.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    /**
     * Return user from username
     * @param username
     * @return
     */
    User findByUsername(String username);
}
