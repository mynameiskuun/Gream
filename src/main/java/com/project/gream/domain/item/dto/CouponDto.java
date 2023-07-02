package com.project.gream.domain.coupon;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
public class CouponDto {

    private Long id;
    private String type;
    private String name;
    private int discountRate;
    private LocalDateTime expireDate;
    private int stock;
    private int minOrderPrice;
    private int validPeriod;


    @Builder
    public CouponDto(Long id, String type, String name, int discountRate, LocalDateTime expireDate, int stock, int minOrderPrice, int validPeriod) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.discountRate = discountRate;
        this.expireDate = expireDate;
        this.stock = stock;
        this.minOrderPrice = minOrderPrice;
        this.validPeriod = validPeriod;
    }

    public Coupon toEntity() {
        return Coupon.builder()
                .id(id)
                .type(type)
                .name(name)
                .discountRate(discountRate)
                .expireDate(expireDate)
                .stock(stock)
                .minOrderPrice(minOrderPrice)
                .validPeriod(validPeriod)
                .build();
    }

    public static CouponDto fromEntity(Coupon coupon) {
        return CouponDto.builder()
                .id(coupon.getId())
                .type(coupon.getType())
                .name(coupon.getName())
                .discountRate(coupon.getDiscountRate())
                .expireDate(coupon.getExpireDate())
                .stock(coupon.getStock())
                .minOrderPrice(coupon.getMinOrderPrice())
                .validPeriod(coupon.getValidPeriod())
                .build();
    }
}
