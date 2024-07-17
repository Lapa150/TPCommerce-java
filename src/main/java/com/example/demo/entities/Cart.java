package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Entity
@Table(name = "carritos")
@NoArgsConstructor @ToString @EqualsAndHashCode
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter private long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    @Getter @Setter private Client client;

    @ManyToMany
    @JoinTable(
            name = "cart_products",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @Getter @Setter private List<Product> products;

    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date creationDate = new Date();

    @Getter @Setter private double total;

    @ElementCollection
    @CollectionTable(name = "cart_quantities", joinColumns = @JoinColumn(name = "cart_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    @Getter @Setter private Map<Long, Integer> quantities = new HashMap<>();

    public void calculateTotal() {
        this.total = products.stream()
                .mapToDouble(product -> product.getPrice() * quantities.get(product.getId()))
                .sum();
    }
}
