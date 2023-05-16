package com.project.gream.domain.item.entity;

import com.project.gream.common.util.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
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
    private int validPeriod;

    @Builder
    public Coupon(Long id, String type, String name, int discountRate, LocalDateTime expireDate, int stock, int minOrderPrice, int validPeriod) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.discountRate = discountRate;
        this.expireDate = expireDate;
        this.stock = stock;
        this.minOrderPrice = minOrderPrice;
        this.validPeriod = validPeriod;
    }
}
