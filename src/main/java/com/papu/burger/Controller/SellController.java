package com.papu.burger.Controller;

import com.papu.burger.Model.Product;
import com.papu.burger.Model.Sell;
import com.papu.burger.Model.SellRequest;
import com.papu.burger.Repository.DayBookRepository;
import com.papu.burger.Repository.ProductRepository;
import com.papu.burger.Repository.SellRepository;
import com.papu.burger.Service.DayBookService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sells")
@AllArgsConstructor
public class SellController {
    private final SellRepository sellRepository;
    private final ProductRepository productRepository;
    private final DayBookService dayBookService;
    private final DayBookRepository dayBookRepository;

    @GetMapping("")
    public ResponseEntity<?> getSells() {
        return ResponseEntity.ok().body(sellRepository.findAll());
    }

    @GetMapping("/daybook")
    public ResponseEntity<?> getDayBooks() {
        return ResponseEntity.ok().body(dayBookRepository.findAll());
    }

    @PostMapping("")
    public ResponseEntity<?> createSell(@RequestBody List<SellRequest> sellRequests, @RequestParam String paymentType) {
        try {

            for (SellRequest sellRequest : sellRequests) {
                System.out.println(sellRequest);
            }
            List<Sell> sells = new ArrayList<>();
            for (SellRequest sellRequest : sellRequests) {

                Product product = productRepository.findById(sellRequest.getId())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + sellRequest.getName()));

                if (product.getStock() < sellRequest.getQuantity()) {
                    return ResponseEntity.badRequest().body("Stock insuficiente para el producto: " + product.getName());
                }

                Sell sell = Sell.builder()
                        .product(product)
                        .price(sellRequest.getPrice())
                        .qty(sellRequest.getQuantity())
                        .saleDate(LocalDateTime.now())
                        .build();

                sellRepository.save(sell);
                sells.add(sell);

                product.setStock(product.getStock() - sellRequest.getQuantity());
                productRepository.save(product);
                dayBookService.updateDayBook(sells, paymentType);
            }


            return ResponseEntity.ok("Ventas registradas exitosamente y stock actualizado");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear las ventas: " + e.getMessage());
        }
    }
}
