package com.example.demo.option;


import com.example.demo.product.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

public class OptionResponse {

    @Data
    @NoArgsConstructor
    public static class FindByProductIdDTO{
        private Long id;

        private Long productId;

        private String optionName;

        private Long Price;

        private int quantity;

        public Option toEntity(){
            return Option.builder()
                    .optionName(optionName)
                    .price(Price)
                    .quantity(quantity)
                    .build();
        }

        public FindByProductIdDTO(Option option) {
            this.id = option.getId();
            this.productId = option.getProduct().getId();
            this.optionName = option.getOptionName();
            this.Price = option.getPrice();
            this.quantity = option.getQuantity();
        }
    }

    @Data
    @NoArgsConstructor
    public static class FindAll{
        private Long id;

        private Long productId;

        private String optionName;

        private Long Price;

        private int quantity;

        public Option toEntity(){
            return Option.builder()
                    .optionName(optionName)
                    .price(Price)
                    .quantity(quantity)
                    .build();
        }

        public static OptionResponse.FindAll toOptionDTO(Option option, Long productId){
            OptionResponse.FindAll findAll = new FindAll();
            findAll.setId(option.getId());
            findAll.setOptionName(option.getOptionName());
            findAll.setPrice(option.getPrice());
            findAll.setQuantity(option.getQuantity());
            findAll.setProductId(productId);
            return findAll;
        }

        public FindAll(Option option) {
            this.id = option.getId();
            this.productId = option.getProduct().getId();
            this.optionName = option.getOptionName();
            this.Price = option.getPrice();
            this.quantity = option.getQuantity();
        }
    }
}
