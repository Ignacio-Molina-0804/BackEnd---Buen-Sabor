package com.papu.burger.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DayBook {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String description;
    private double amount;
    private String type; // debit, crypto or cash
    private String entryType; // DEBE or HABER

    @ManyToOne
    @JoinColumn(name = "sell_id", nullable = true)
    private Sell reference;  // reference to the sale or other transaction

    private Date createdAt; // date of the transaction
}