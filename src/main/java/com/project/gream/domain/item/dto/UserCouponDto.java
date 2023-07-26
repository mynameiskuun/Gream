package com.project.gream.domain.item.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.project.gream.common.enumlist.CouponStatus;
import com.project.gream.domain.member.dto.MemberDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

public class UserCouponDto {

    @NoArgsConstructor
    @Getter
    @Setter
    public static class Response {

        private Long id;
        private CouponStatus status;
        private CouponDto couponDto;
        private MemberDto memberDto;

        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        private LocalDateTime createdTime;
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        private LocalDateTime modifiedTime;

        @Builder
        public Response(Long id, CouponStatus status, CouponDto couponDto, MemberDto memberDto,
                                     LocalDateTime createdTime, LocalDateTime modifiedTime) {
            this.id = id;
            this.status = status;
            this.couponDto = couponDto;
            this.memberDto = memberDto;
            this.createdTime = createdTime;
            this.modifiedTime = modifiedTime;
        }

        @Builder
        public Response(UserCouponVO couponVO) {
            this.id = couponVO.getId();
            this.status = couponVO.getStatus();
            this.couponDto = couponVO.getCouponDto();
            this.memberDto = couponVO.getMemberDto();
        }
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class CouponApplyRequest {

        private Long itemId;
        private Long cartItemId;
        private Long userCouponId;

        @Builder
        public CouponApplyRequest(Long itemId, Long cartItemId, Long userCouponId) {
            this.itemId = itemId;
            this.cartItemId = cartItemId;
            this.userCouponId = userCouponId;
        }
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class CouponApplyResponse {

        private int singleItemPrice;
        private double discountAmount;
        private double afterDiscountPrice;
        private String message;

        @Builder
        public CouponApplyResponse(int singleItemPrice, double discountAmount, double afterDiscountPrice, String message) {
            this.singleItemPrice = singleItemPrice;
            this.discountAmount = discountAmount;
            this.afterDiscountPrice = afterDiscountPrice;
            this.message = message;
        }
    }
}
