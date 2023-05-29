package com.project.gream.domain.coupon;

import com.project.gream.common.util.BaseTimeEntity;
import com.project.gream.domain.coupon.Coupon;
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

    @Builder
    public CouponBox(Long id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }
}
