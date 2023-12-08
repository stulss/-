package com.example.demo.product;

import com.example.demo.core.utils.ApiUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {

    private static final String FILE_DIRECTORY = "C:/Users/G/Desktop/green/Board Files/";
    private final ProductService productService;

    // 상품 등록
    @PostMapping("/save")
    public ResponseEntity<?> createProduct(@ModelAttribute ProductResponse.create product,
                                           @RequestParam MultipartFile[] files) throws IOException {
        productService.createProduct(product,files);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(null);
        return ResponseEntity.ok(apiResult);
    }

    // 전체 상품 확인
    @PostMapping("/find/")
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

    @GetMapping("/image/{uuid}/{fileName}")
    public ResponseEntity<?> serveImage(@PathVariable String uuid, @PathVariable String fileName) throws IOException {
        Path filePath = Paths.get(FILE_DIRECTORY + uuid + fileName);
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // 이미지 타입에 따라 적절히 변경하세요 (예: MediaType.IMAGE_PNG)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            // 이미지를 찾을 수 없을 경우에 대한 처리 (예: 기본 이미지 반환 또는 에러 처리)
            return ResponseEntity.notFound().build();
        }
    }
}