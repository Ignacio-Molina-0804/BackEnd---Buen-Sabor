package com.papu.burger.Model;

import lombok.Data;

import java.util.UUID;

@Data
public class SellRequest {
    private UUID id;
    private String name;
    private double price;
    private String description;
    private int quantity;
}

