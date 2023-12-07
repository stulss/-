package com.example.demo.cart;

import com.example.demo.member.Member;
import com.example.demo.option.Option;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "cart_tb",
        indexes = {
            @Index(name = "cart_member_id_index", columnList = "member_id"),
            @Index(name = "cart_option_id_index", columnList = "option_id")
        },
        uniqueConstraints = {
            @UniqueConstraint(name = "uk_cart_option_member", columnNames = {"member_id","option_id"})
        })
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 유저별로 카트에 묶임
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    private Option option;

    @Column(nullable = false)
    private Long quantity;

    @Column(nullable = false)
    private Long price;

    @Builder
    public Cart(Long id,Member member, Option option, Long quantity, Long price) {
        this.id = id;
        this.member = member;
        this.option = option;
        this.quantity = quantity;
        this.price = price;
    }

    public void update(Long quantity, Long price){
        this.quantity = quantity;
        this.price = price;
    }
}
