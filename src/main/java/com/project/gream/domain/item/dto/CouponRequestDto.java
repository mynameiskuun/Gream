package com.project.gream.domain.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;
import java.util.List;

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
    private List<String> discountFor;
    private String memberId;
    private List<Long> couponIdList;

    @Builder
    public CouponRequestDto(String type, String name, int discountRate, LocalDateTime expireDate, int stock,
                            int minOrderPrice, List<String> discountFor, String memberId, List<Long> couponIdList) {
        this.type = type;
        this.name = name;
        this.discountRate = discountRate;
        this.expireDate = expireDate;
        this.stock = stock;
        this.minOrderPrice = minOrderPrice;
        this.discountFor = discountFor;
        this.memberId = memberId;
        this.couponIdList = couponIdList;
    }
}
