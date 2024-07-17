package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "productos")
@NoArgsConstructor @ToString @EqualsAndHashCode
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter private long id;

    @Getter @Setter private String name;
    @Getter @Setter private int stock;
    @Getter @Setter private double price;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter @Setter private List<Cart> carts;
}
