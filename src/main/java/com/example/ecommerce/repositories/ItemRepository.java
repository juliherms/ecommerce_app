package com.example.ecommerce.repositories;

import com.example.ecommerce.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    /**
     * list itens from name
     * @param name
     * @return
     */
    List<Item> findByName(String name);
}
