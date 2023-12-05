package com.example.demo.product;

import com.example.demo.core.error.exception.Exception404;
import com.example.demo.option.Option;
import com.example.demo.option.OptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;

    @Transactional
    public Product createProduct(ProductResponse.create createDto) {
        Product product = createDto.toEntity();
        return productRepository.save(product);
    }

    // 전체 상품 검색
    public List<ProductResponse.FindAllDTO> findAll(int page) {
        Pageable pageable = PageRequest.of(page,2);
        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductResponse.FindAllDTO> productDTOS =
                productPage.getContent().stream().map(ProductResponse.FindAllDTO::new)
                .collect(Collectors.toList());
        return productDTOS;
    }

    // 개별 상품 검색
    public ProductResponse.FindByIdDTO findByProductId(Long id) {
        // List<Option> -> ProductResponse.FindByIdDTO
        //dto안에 리스트가 포함되어 있기때문에 바로 dto를 반환
        Product product = productRepository.findById(id).orElseThrow(
                () -> new Exception404("해당 상품("+id+")은 등록되어 있지 않습니다.") );

        // product.getId() 로  Option 상품 검색
        List<Option> optionList = optionRepository.findByProductId(product.getId());

        // 검색이 완료된 제품 반환.
        return new ProductResponse.FindByIdDTO(product,optionList);
    }

    @Transactional
    public void updateProduct(ProductResponse.FindByIdDTO productDTO) {
        Optional<Product> productOptional = productRepository.findById(productDTO.getId());

        if(productOptional.isPresent()){
            Product product = productOptional.get();
            product.update(productDTO.toEntity());
            productRepository.save(product);
        }


    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}