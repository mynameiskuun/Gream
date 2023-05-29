package com.project.gream.domain.coupon;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CouponBoxDto {

    private Long id;
    private int quantity;
    private CouponDto couponDto;

    @Builder
    public CouponBoxDto(Long id, int quantity, CouponDto couponDto) {
        this.id = id;
        this.quantity = quantity;
        this.couponDto = couponDto;
    }

    public CouponBox toEntity() {
        return CouponBox.builder()
                .id(this.id)
                .quantity(this.quantity)
//                .coupon(couponDto.toEntity())
                .build();
    }

    public static CouponBoxDto fromEntity(CouponBox couponBox) {
        return CouponBoxDto.builder()
                .id(couponBox.getId())
                .quantity(couponBox.getQuantity())
//                .couponDto(CouponDto.fromEntity(couponBox.getCoupon()))
                .build();
    }
}
