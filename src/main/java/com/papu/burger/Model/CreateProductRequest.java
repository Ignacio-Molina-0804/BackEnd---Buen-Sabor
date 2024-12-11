package com.papu.burger.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@Builder
public class CreateProductRequest {
    String name;
    double price;
    String description;
    String category;
    int stock;
    int maxStock;
    MultipartFile image;
}
