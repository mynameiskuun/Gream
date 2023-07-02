package com.project.gream.domain.item.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.project.gream.domain.item.entity.Coupon;
import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

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
    private String discountFor;
    private MemberDto memberDto;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdTime;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime modifiedTime;

    @Builder
    public CouponDto(Long id, String type, String name, int discountRate, LocalDateTime expireDate, int stock, int minOrderPrice,
                     String discountFor, MemberDto memberDto, LocalDateTime createdTime, LocalDateTime modifiedTime) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.discountRate = discountRate;
        this.expireDate = expireDate;
        this.stock = stock;
        this.minOrderPrice = minOrderPrice;
        this.discountFor = discountFor;
        this.memberDto = memberDto;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
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
                .discountFor(discountFor)
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
                .discountFor(coupon.getDiscountFor())
                .createdTime(coupon.getCreatedTime())
                .modifiedTime(coupon.getModifiedTime())
                .build();
    }

}
