package com.example.demo.order;

import com.example.demo.cart.Cart;
import com.example.demo.cart.CartRepository;
import com.example.demo.core.error.exception.Exception404;
import com.example.demo.core.error.exception.Exception500;
import com.example.demo.member.Member;
import com.example.demo.order.item.Item;
import com.example.demo.order.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;

    // 결제 시도시 작동
    @Transactional
    public OrderResponse.FindByIdDTO save(Member member) {
        //장바구니 조회
        List<Cart> cartList = cartRepository.findAllByMemberId(member.getId());

        if(cartList.size() == 0){
           throw new Exception404("장바구니에 상품 내역이 존재하지 않습니다.");
        }

        // 주문 생성
        Order order = orderRepository.save
                (Order.builder().member(member).build());
        // 아이템 저장
        List<Item> itemList = new ArrayList<>();
        for(Cart cart:cartList){
            Item item = Item.builder()
                    .option(cart.getOption())
                    .order(order)
                    .quantity(cart.getQuantity())
                    .price(cart.getOption().getPrice() * cart.getQuantity())
                    .build();

            itemList.add(item);}

        try{
            itemRepository.saveAll(itemList);
        }catch (Exception e){
            throw new Exception500("주문 생성중 오류가 발생하였습니다.");
        }
        return new OrderResponse.FindByIdDTO(order,itemList);
    }


    public OrderResponse.FindByIdDTO findById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(
            () -> new Exception404("해당주문 내역을 찾을 수 없습니다."+ id));

        List<Item> itemList = itemRepository.findAllByOrderId(id);
        return new OrderResponse.FindByIdDTO(order,itemList);
    }

    @Transactional
    public void delete() {
        try{
            itemRepository.deleteAll();
        }catch (Exception e){
            throw new Exception500("아이템 삭제 오류"+ e.getMessage());
        }
    }
}
