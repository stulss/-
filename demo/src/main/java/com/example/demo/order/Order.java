package com.example.demo.order;

import com.example.demo.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "order_tb",
        indexes = {
                @Index(name = "order_member_id_index", columnList = "member_id")
        })
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    public Order(Long id, Member member) {
        this.id = id;
        this.member = member;
    }
}
