package com.project.gream.domain.item.entity;

import com.project.gream.common.util.BaseTimeEntity;
import com.project.gream.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class CouponBox extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "couponbox_id")
    private Long id;
    @Column(name = "coupon_quantity")
    private int quantity;
    @OneToOne(mappedBy = "couponbox_id")
    private Member member;

    @Builder
    public CouponBox(Long id, int quantity, Member member) {
        this.id = id;
        this.quantity = quantity;
        this.member = member;
    }
}
