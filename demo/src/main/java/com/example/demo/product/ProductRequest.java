package com.example.demo.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductRequest {
    // PK
    private Long id;

    // 상품명(필수)
    private String productName;

    // 상품 설명(필수)
    private String description;

    // 이미지 정보
    private String image;

    // 가격
    private String price;

    public Product toEntity(){
        return Product.builder()
                .productName(productName)
                .description(description)
                .image(image)
                .price(price)
                .build();
    }
}
