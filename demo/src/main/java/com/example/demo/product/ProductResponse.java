package com.example.demo.product;

import com.example.demo.file.ProductFile;
import com.example.demo.option.Option;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;


public class ProductResponse {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class create{
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

        public create(Product product,ProductFile productFile){
            this.id = product.getId();
            this.productName = product.getProductName();
            this.description = product.getDescription();
            this.image = productFile.getFilePath();
            this.price = product.getPrice();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class FindAllDTO{
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
                    .id(id)
                    .productName(productName)
                    .description(description)
                    .image(image)
                    .price(price)
                    .build();
        }

        public FindAllDTO(Product product) {
                this.id = product.getId();
                this.productName = product.getProductName();
                this.description = product.getDescription();
                this.image = product.getImage();
                this.price = product.getPrice();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class FindByIdDTO{
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

        private List<OptionDTO> optionList;

        public Product toEntity(){
            return Product.builder()
                    .id(id)
                    .productName(productName)
                    .description(description)
                    .image(image)
                    .price(price)
                    .build();
        }

        public FindByIdDTO(Product product,List<Option> optionList) {
            this.id = product.getId();
            this.productName = product.getProductName();
            this.description = product.getDescription();
            this.image = product.getImage();
            this.price = product.getPrice();
            this.optionList = optionList.stream().map(OptionDTO::new)
                    .collect(Collectors.toList());
        }
    }

    @Data
    public static class OptionDTO{
        private Long id;
        private String optionName;
        private Long price;
        private int quantity;

        public OptionDTO(Option option) {
            this.id = option.getId();
            this.optionName = option.getOptionName();
            this.price = option.getPrice();
            this.quantity = option.getQuantity();
        }
    }

    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PagingDto{
        // PK
        private Long id;

        // 상품명(필수)
        private String productName;

        // 이미지 정보
        private String imagePath;

        // 가격
        private String price;

        public Product toHomepage(){
            return Product.builder()
                    .id(id)
                    .productName(productName)
                    .image(imagePath)
                    .price(price)
                    .build();
        }
    }
}