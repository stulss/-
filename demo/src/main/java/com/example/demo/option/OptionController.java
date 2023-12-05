package com.example.demo.option;


import com.example.demo.core.utils.ApiUtils;
import com.example.demo.product.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OptionController {

    private final OptionService optionService;

    @PostMapping("/option/save")
    public Option save(@RequestBody OptionResponse.FindAll findAll){
        Option option = optionService.save(findAll);
        List<OptionResponse.FindAll> findAlls = optionService.findProductAll(findAll.getProductId());
        return optionService.save(findAll);
    }

    /**
     *
     * @param id
     * ProductId
     * @return
     * List'<'OptionResponse.FindByProductIdDTO>
     */
    @GetMapping("/products/{id}/options")
    public ResponseEntity<?> findById(@PathVariable Long id){
        List<OptionResponse.FindByProductIdDTO> productDTOS = optionService.findByProductId(id);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(productDTOS);
        return ResponseEntity.ok(apiResult);
    }

    @GetMapping("/options")
    public ResponseEntity<?> findAll(){
        List<OptionResponse.FindAll> findAlls = optionService.findAll();
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(findAlls);
        return ResponseEntity.ok(apiResult);
    }

    @PostMapping("/options/{id}/update")
    public ResponseEntity<?> updateOption(@PathVariable Long id, @RequestBody OptionResponse.FindByProductIdDTO optionDto) {
        optionDto.setId(id); // 요청 경로의 ID가 우선적으로 적용되도록 합니다.
        optionService.updateOption(optionDto);

        // 업데이트된 옵션 정보를 반환합니다.
        List<OptionResponse.FindByProductIdDTO> updatedOption = optionService.findByProductId(id);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(updatedOption);
        return ResponseEntity.ok(apiResult);
    }

    @DeleteMapping("/options/{id}/delete")
    public void deleteOption(@PathVariable Long id) {
        optionService.deleteOption(id);
    }
}