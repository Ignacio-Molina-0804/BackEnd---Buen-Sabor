package com.papu.burger.Controller;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.papu.burger.Model.BinancePayRequest;
import com.papu.burger.Service.BinanceService;
import com.papu.burger.Service.MercadoPagoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@AllArgsConstructor
public class PaymentController {

    private MercadoPagoService mercadoPagoService;
    private BinanceService binanceService;

    //Binance
    @PostMapping("/createbinance")
    public ResponseEntity<?> createPaymentMercadoPago(@RequestBody BinancePayRequest binancePayRequest) throws Exception {
        try {
            return ResponseEntity.ok().body(binanceService.createPayment(binancePayRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Mercado Pago
    @PostMapping("/createmp")
    public ResponseEntity<?> createPaymentBinance(@RequestParam String title,
                                                  @RequestParam Double price,
                                                  @RequestParam int quantity
    ) {
        try {
            return ResponseEntity.ok().body(mercadoPagoService.createPreference(title, price, quantity));
        } catch (MPException e) {
            return ResponseEntity.badRequest().body(e);
        } catch (MPApiException e) {
            return ResponseEntity.badRequest().body(e);
        }
    }
}