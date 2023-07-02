package com.project.gream.domain.item.dto;

import com.project.gream.common.enumlist.CouponStatus;
import com.project.gream.domain.item.entity.UserCoupon;
import com.project.gream.domain.member.dto.MemberDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserCouponVO {

    private Long id;
    private CouponStatus status;
    private CouponDto couponDto;
    private MemberDto memberDto;

    @Builder
    public UserCouponVO(Long id, CouponStatus status, CouponDto couponDto, MemberDto memberDto) {
        this.id = id;
        this.status = status;
        this.couponDto = couponDto;
        this.memberDto = memberDto;
    }

    public UserCoupon toEntity() {
        return UserCoupon.builder()
                .id(id)
                .status(status)
                .coupon(couponDto.toEntity())
                .member(memberDto.toEntity())
                .build();
    }

    public static UserCouponVO fromEntity(UserCoupon userCoupon) {
        return UserCouponVO.builder()
                .id(userCoupon.getId())
                .status(userCoupon.getStatus())
                .couponDto(CouponDto.fromEntity(userCoupon.getCoupon()))
                .memberDto(MemberDto.fromEntity(userCoupon.getMember()))
                .build();
    }
}
