package com.example.demo.cart;

import com.example.demo.core.security.CustomUserDetails;
import com.example.demo.core.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    // 카트에 상품 추가
    @PostMapping("/carts/add")
    public ResponseEntity<?> addCartList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody @Valid List<CartRequest.SaveDTO> saveDTOS,
            Error error){
        cartService.addCartList(saveDTOS, customUserDetails.getMember());

        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(null);
        return ResponseEntity.ok(apiResult);
    }

    // 카트 업데이트
    @GetMapping("/carts/update")
    public ResponseEntity<?> update(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody @Valid List<CartRequest.UpdateDTO> requestDTOS,
            Error error){
        CartResponse.UpdateDTO updateDTO = cartService.update(requestDTOS,customUserDetails.getMember());
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(updateDTO);
        return ResponseEntity.ok(apiResult);
    }

    // 카트 전체 상품 확인.
    @GetMapping("/carts")
    public ResponseEntity<?> carts(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        CartResponse.FindAllDTO findAllDTO = cartService.findAll();
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(findAllDTO);
        return ResponseEntity.ok(apiResult);
    }
}