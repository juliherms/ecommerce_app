package com.example.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * This class responsible to represent Cart
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    @Column
    private Long id;

    @ManyToMany
    @JsonProperty
    @Column
    private List<Item> items;

    @OneToOne(mappedBy = "cart")
    @JsonProperty
    private User user;

    @Column
    @JsonProperty
    private BigDecimal total;
}
