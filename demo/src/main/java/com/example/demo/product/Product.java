package com.example.demo.product;

import com.example.demo.option.Option;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 상품명(필수)
    @Column(length = 100, nullable = false)
    private String productName;

    // 상품 설명(필수)
    @Column(length = 1000, nullable = false)
    private String description;

    // 이미지 정보
    @Column(length = 100)
    private String image;

    // 가격
    private String price;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Option> options = new ArrayList<>();

    @Builder
    public Product(Long id, String productName, String description, String image, String price) {
        this.id = id;
        this.productName = productName;
        this.description = description;
        this.image = image;
        this.price = price;
    }

    public void update(Product product) {
        this.productName = product.getProductName();
        this.description = product.getDescription();
        this.image = product.getImage();
        this.price = product.getPrice();
    }
}