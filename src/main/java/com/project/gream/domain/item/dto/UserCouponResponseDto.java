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

@NoArgsConstructor
@Getter
@Setter
public class UserCouponResponseDto {

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
    public UserCouponResponseDto(Long id, CouponStatus status, CouponDto couponDto, MemberDto memberDto,
                                 LocalDateTime createdTime, LocalDateTime modifiedTime) {
        this.id = id;
        this.status = status;
        this.couponDto = couponDto;
        this.memberDto = memberDto;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
    }

    @Builder
    public UserCouponResponseDto(UserCouponVO couponVO) {
        this.id = couponVO.getId();
        this.status = couponVO.getStatus();
        this.couponDto = couponVO.getCouponDto();
        this.memberDto = couponVO.getMemberDto();

    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class CouponApplyRequest {

        private Long itemId;
        private Long couponId;

        @Builder
        public CouponApplyRequest(Long itemId, Long couponId) {
            this.itemId = itemId;
            this.couponId = couponId;
        }
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class CouponApplyResponse {

        private int originPrice;
        private int discountAmount;
        private int afterDiscountPrice;
        private String message;

        @Builder
        public CouponApplyResponse(int originPrice, int discountAmount, int afterDiscountPrice, String message) {
            this.originPrice = originPrice;
            this.discountAmount = discountAmount;
            this.afterDiscountPrice = afterDiscountPrice;
            this.message = message;
        }
    }
}
