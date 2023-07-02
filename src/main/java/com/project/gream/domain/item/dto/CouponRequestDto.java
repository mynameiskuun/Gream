package com.project.gream.domain.coupon;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CouponRequestDto {

    private String type;
    private String name;
    private int discountRate;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expireDate;
    private int stock;
    private int minOrderPrice;

    @Builder
    public CouponRequestDto(String type, String name, int discountRate, LocalDateTime expireDate, int stock, int minOrderPrice) {
        this.type = type;
        this.name = name;
        this.discountRate = discountRate;
        this.expireDate = expireDate;
        this.stock = stock;
        this.minOrderPrice = minOrderPrice;
    }
}
