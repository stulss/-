package com.example.demo.cart;


import com.example.demo.member.Member;
import com.example.demo.option.Option;
import com.example.demo.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.List;
import java.util.stream.Collectors;


public class CartResponse {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class FindAllDTO{
        List<ProductDTO> products;

        private Long totalPrice;

        public FindAllDTO(List<Cart> cartList){
            this.products = cartList.stream()
                    .map(cart -> cart.getOption().getProduct()).distinct()
                    .map(product -> new ProductDTO(cartList,product)).collect(Collectors.toList());
            this.totalPrice = cartList.stream()
                    .mapToLong(cart -> cart.getOption().getPrice() * cart.getQuantity())
                    .sum();
        }


        @Data
        public class ProductDTO{
            private Long id;
            private String productName;
            List<CartDTO> cartDTOS;

            public ProductDTO(List<Cart> cartList,Product product) {
                this.id = product.getId();
                this.productName = product.getProductName();
                this.cartDTOS = cartList.stream()
                        .filter(cart -> cart.getOption().getProduct().getId() == product.getId())
                        .map(CartDTO::new).collect(Collectors.toList());
            }

            @Data
            public class CartDTO{
                private Long id;
                private OptionDTO optionDTO;
                private Long price;
                private Long quantity;
                public CartDTO(Cart cart) {
                    this.id = cart.getId();
                    this.optionDTO = new OptionDTO(cart.getOption());
                    this.quantity = cart.getQuantity();
                    this.price = cart.getPrice();
                }

                @Data
                public class OptionDTO{

                    private Long id;
                    private String optionName;
                    private Long price;

                    public OptionDTO(Option option) {
                        this.id = option.getId();
                        this.optionName = option.getOptionName();
                        this.price = option.getPrice();
                    }
                }
            }
        }
    }

    @Data
    public static class UpdateDTO{
        private List<CartDTO> dtoList;
        private Long totalPrice;

        public UpdateDTO(List<Cart> dtoList) {
            this.dtoList = dtoList.stream().map(CartDTO::new).collect(Collectors.toList());
            this.totalPrice = totalPrice;
        }

        @Data
        public class CartDTO{

            private Long cartId;

            private Long optionId;

            private String optionName;

            private Long quantity;

            private Long price;

            public CartDTO(Cart cart) {
                this.cartId = cart.getId();
                this.optionId = cart.getOption().getId();
                this.optionName = cart.getOption().getOptionName();
                this.quantity = cart.getQuantity();
                this.price = cart.getPrice();
            }
        }

    }

}
