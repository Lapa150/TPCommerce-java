package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "facturas")
@NoArgsConstructor @ToString @EqualsAndHashCode
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter private long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    @Getter @Setter private Client client;

    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date invoiceDate = new Date();

    //precio total
    @Getter @Setter private double totalAmount;
}
