package com.papu.burger.Model;

import lombok.Data;

@Data
public class BinancePayRequest { // Identificador del pedido del comerciante
    private String orderAmount;     // Monto del pedido
    private String currency;        // Moneda (por ejemplo, "USD")
    private String message;
}
