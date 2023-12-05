package com.example.demo.product;

import com.example.demo.core.utils.ApiUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    // 상품 등록
    @PostMapping("/save")
    public Product createProduct(@RequestBody ProductResponse.create product) {
        return productService.createProduct(product);
    }

    // 전체 상품 확인
    @GetMapping("/find/")
    public ResponseEntity<?> findAll(@RequestParam(defaultValue = "0") int page){
        List<ProductResponse.FindAllDTO> productDTOS = productService.findAll(page);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(productDTOS);
        return ResponseEntity.ok(apiResult);
    }

    //개별 상품 확인
    @GetMapping("/find/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        ProductResponse.FindByIdDTO productDTOS = productService.findByProductId(id);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(productDTOS);
        return ResponseEntity.ok(apiResult);
    }

    // 상품 수정
    @PostMapping("/{id}/update")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody ProductResponse.FindByIdDTO productDto) {
        productDto.setId(id); // 요청 경로의 ID가 우선적으로 적용되도록 합니다.
        productService.updateProduct(productDto);

        // 업데이트된 상품 정보를 반환합니다.
        ProductResponse.FindByIdDTO updatedProduct = productService.findByProductId(id);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(updatedProduct);
        return ResponseEntity.ok(apiResult);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}