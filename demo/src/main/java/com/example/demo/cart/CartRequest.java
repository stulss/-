package com.example.demo.cart;

import com.example.demo.member.Member;
import com.example.demo.option.Option;
import lombok.Data;

import java.util.List;

public class CartRequest {
    @Data
    public static class SaveDTO {
        private Long optionId;
        private Long quantity;

        public Cart toEntity(Option option, Member member){
            return Cart.builder()
                    .option(option)
                    .member(member)
                    .quantity(quantity)
                    .price(option.getPrice() * quantity)
                    .build();
        }
    }

    @Data
    public static class UpdateDTO{
        private Long cartId;
        private Long quantity;
    }

}
