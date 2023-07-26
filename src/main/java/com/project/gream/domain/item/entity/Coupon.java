package com.project.gream.domain.item.entity;

import com.project.gream.common.enumlist.converter.CouponDiscountForConverter;
import com.project.gream.common.util.BaseTimeEntity;
import com.project.gream.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Coupon extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;
    @Column(name = "coupon_type")
    private String type;
    @Column(name = "coupon_name")
    private String name;
    private int discountRate;
    private LocalDateTime expireDate;
    @Column(name = "coupon_stock")
    private int stock;
    private int minOrderPrice;
    private String discountFor;

    @Builder
    public Coupon(Long id, String type, String name, int discountRate, LocalDateTime expireDate,
                  int stock, int minOrderPrice, String discountFor) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.discountRate = discountRate;
        this.expireDate = expireDate;
        this.stock = stock;
        this.minOrderPrice = minOrderPrice;
        this.discountFor = discountFor;
    }

    public boolean isExpired() {
        return this.expireDate.isBefore(LocalDateTime.now());
    }
}
