package com.example.demo.cart;

import com.example.demo.core.error.exception.Exception400;
import com.example.demo.core.error.exception.Exception404;
import com.example.demo.core.error.exception.Exception500;
import com.example.demo.member.Member;
import com.example.demo.option.Option;
import com.example.demo.option.OptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final OptionRepository optionRepository;

    public CartResponse.FindAllDTO findAll() {
        List<Cart> cartList = cartRepository.findAll();
        return new CartResponse.FindAllDTO(cartList);
    }

    // 카트에 상품 추가
    @Transactional
    public void addCartList(List<CartRequest.SaveDTO> saveDTOS, Member member) {
        // 동일한 데이터를 묶어주는 컨테이너
        // 동일한 상품 예외처리
        Set<Long> optionsId = new HashSet<>();

        for(CartRequest.SaveDTO cart : saveDTOS){
            if(!optionsId.add(cart.getOptionId()));
            throw new Exception400("이미 동일한 상품의 옵션이 있습니다." + cart.getOptionId());
        }

        List<Cart> cartList = saveDTOS.stream().map(cartDTO -> {
                    Option option = optionRepository.findById(cartDTO.getOptionId()).orElseThrow(
                            () -> new Exception404("해당 상품 옵션을 찾을 수 없습니다." + cartDTO.getOptionId()));
                    return cartDTO.toEntity(option,member);
                }).collect(Collectors.toList());

        cartList.forEach(cart -> {
            try{
                cartRepository.save(cart);
            } catch (Exception e){
                throw new Exception500("카트저장에 실패하였습니다."+e.getMessage());
            }});
    }

    /*
        request = 요청
        response = 응답
     */
    @Transactional
    public CartResponse.UpdateDTO update(List<CartRequest.UpdateDTO> requestDTOS, Member member) {
        List<Cart> cartList = cartRepository.findAllByMemberId(member.getId());

        //카트아이디에 있는 pk만 가져온다.
        List<Long> cartIds = cartList.stream().map(cart -> cart.getId()).collect(Collectors.toList());
        List<Long> requestIds = requestDTOS.stream().map(dto -> dto.getCartId()).collect(Collectors.toList());

        if(cartIds.size() == 0){
            throw new Exception404("주문 가능한 상품이 없습니다.");
        }
        if(requestIds.stream().distinct().count() != requestIds.size()){
            throw new Exception400("동일한 카트 아이디를 주문할 수 없습니다.");
        }

        for(Long requestId : requestIds){
            if(!cartIds.contains(requestId)){
                throw new Exception400("카트에 없는 상품은 주문할 수 없습니다."+requestId);
            }
        }

        for(CartRequest.UpdateDTO updateDTO : requestDTOS){
            for(Cart cart : cartList){
                if(cart.getId() == updateDTO.getCartId()){
                    cart.update(updateDTO.getQuantity(), cart.getPrice() * cart.getQuantity());
                }
            }
        }
        return new CartResponse.UpdateDTO(cartList);
    }
}
