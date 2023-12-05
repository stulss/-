package com.example.demo.option;

import com.example.demo.product.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "options_tb",
        indexes = {
        @Index(name = "option_product_id_index", columnList = "product_id")
        })
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    // 옵션상품 이름
    @Column(length = 100, nullable = false)
    private String optionName;

    // 옵션상품 가격
    private Long Price;

    //옵션상품 수량
    private int quantity;

    @Builder
    public Option(Long id, Product product, String optionName, Long price, int quantity) {
        this.id = id;
        this.product = product;
        this.optionName = optionName;
        this.Price = price;
        this.quantity = quantity;
    }

    public void updateOption(Option option) {
        this.optionName = option.getOptionName();
        this.Price = option.getPrice();
        this.quantity = option.getQuantity();
    }

    public Option toUpdate(Product product) {
        Option option = new Option();
        this.product = product;
        return option;
    }
}
