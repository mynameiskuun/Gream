package com.project.gream.domain.item.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.project.gream.common.enumlist.CouponStatus;
import com.project.gream.domain.item.entity.UserCoupon;
import com.project.gream.domain.member.dto.MemberDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

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

}
