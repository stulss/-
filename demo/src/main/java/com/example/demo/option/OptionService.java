package com.example.demo.option;

import com.example.demo.product.Product;
import com.example.demo.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OptionService {
    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Option save(OptionResponse.FindAll findAll) {
        Optional<Product> optional = productRepository.findById(findAll.getProductId());

        if(optional.isPresent()){
            Product product = optional.get();
            Option option = findAll.toEntity();
            option.toUpdate(product);
            return optionRepository.save(option);
        } else {
            return null;
        }
    }

    // 옵션 개별상품 검색
    public List<OptionResponse.FindByProductIdDTO> findByProductId(Long id) {
        List<Option> optionList = optionRepository.findByProductId(id);
        List<OptionResponse.FindByProductIdDTO> optionResponses = optionList.stream().map(OptionResponse.FindByProductIdDTO::new)
                .collect(Collectors.toList());
        return optionResponses;
    }
    // 옵션 전체상품 검색
    public List<OptionResponse.FindAll> findAll() {
        List<Option> optionList = optionRepository.findAll();
        List<OptionResponse.FindAll> findAlls = optionList.stream().map(OptionResponse.FindAll::new)
                .collect(Collectors.toList());
        return findAlls;
    }

    @Transactional
    public void updateOption(OptionResponse.FindByProductIdDTO optionDto) {
        Optional<Option> optional = optionRepository.findById(optionDto.getId());

        if(optional.isPresent()){
            Option option = optional.get();
            option.updateOption(optionDto.toEntity());
            optionRepository.save(option);
        }
    }


    @Transactional
    public void deleteOption(Long id) {
        optionRepository.deleteById(id);
    }


    public List<OptionResponse.FindAll> findProductAll(Long productId) {
        Product productEntity = productRepository.findById(productId).get();
        List<Option> optionList = optionRepository.findByProduct(productEntity);

        List<OptionResponse.FindAll> findAlls = new ArrayList<>();
        for(Option option: optionList){
            OptionResponse.FindAll all = OptionResponse.FindAll.toOptionDTO(option, productId);
            findAlls.add(all);
        }
        return findAlls;
    }
}
