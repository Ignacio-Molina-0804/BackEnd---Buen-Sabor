package com.papu.burger.Controller;

import java.util.Map;
import java.util.UUID;

import com.papu.burger.Service.CloudinaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.papu.burger.Model.Product;
import com.papu.burger.Repository.ProductRepository;

import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class ProductController {
    private ProductRepository productRepository;
    private CloudinaryService cloudinaryService;


    @GetMapping("")
    public ResponseEntity<?> getProducts() {
        return ResponseEntity.ok().body(productRepository.findAll());
    }

    @GetMapping("/burgers")
    public ResponseEntity<?> getBurgers() {
        return ResponseEntity.ok().body(productRepository.findByCategory("Hamburguesas"));
    }

    @GetMapping("/drinks")
    public ResponseEntity<?> getDrinks() {
        return ResponseEntity.ok().body(productRepository.findByCategory("Bebidas"));
    }

    @GetMapping("/fries")
    public ResponseEntity<?> getFries() {
        return ResponseEntity.ok().body(productRepository.findByCategory("Papas"));
    }


    @PostMapping("")
    public ResponseEntity<?> createProduct(@RequestParam("name") String name,
                                           @RequestParam("price") double price,
                                           @RequestParam("description") String description,
                                           @RequestParam("category") String category,
                                           @RequestParam("stock") int stock,
                                           @RequestParam("max_stock") int maxStock,
                                           @RequestParam("image") MultipartFile image) {
        try {
            System.out.println(name + image);
            Map<String, Object> uploadResult = cloudinaryService.uploadFile(image);
            String imageUrl = (String) uploadResult.get("url");  // Obtén la URL de la imagen subida

            // Crea el objeto Product con la URL de imagen
            Product product = new Product();
            product.setName(name);
            product.setPrice(price);
            product.setDescription(description);
            product.setCategory(category);
            product.setStock(stock);
            product.setMax_stock(maxStock);
            product.setImage(imageUrl);  // Guarda el enlace de la imagen

            // Guarda el producto en la base de datos
            return ResponseEntity.ok().body(productRepository.save(product));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable UUID id, @RequestBody Product product) {
        try {
            // Buscar el producto en la base de datos
            Product existingProduct = productRepository.findById(id).orElse(null);

            if (existingProduct == null) {
                return ResponseEntity.notFound().build();  // Si no se encuentra el producto, devolver 404
            }

            // Actualizar los campos del producto con los valores recibidos
            existingProduct.setName(product.getName());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setStock(product.getStock());
            existingProduct.setMax_stock(product.getMax_stock());

            // Guardar el producto actualizado en la base de datos
            productRepository.save(existingProduct);

            return ResponseEntity.ok().body(existingProduct);  // Retornar el producto actualizado

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());  // Si ocurre un error, devolver 400
        }
    }
}
