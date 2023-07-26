package com.project.gream.domain.item.service;

import com.project.gream.common.annotation.LoginMember;
import com.project.gream.domain.item.dto.CouponDto;
import com.project.gream.domain.item.dto.CouponRequestDto;
import com.project.gream.domain.item.dto.UserCouponDto;
import com.project.gream.domain.item.dto.UserCouponResponseDto;
import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface DiscountService {

    String createCoupon(CouponRequestDto requestDto);
    Page<CouponDto> getCouponList(Pageable pageable);
    String deleteCoupon(Long couponId);
    List<CouponDto> getUsableCouponList(Long itemId);
    String saveUsableCoupon(CouponRequestDto requestDto);
    List<UserCouponResponseDto> getMemberCoupon(Member member);
    List<UserCouponResponseDto> getMemberCouponForMypage(String memberId);
    Map<String, String> isPointUsable(int point, @LoginMember MemberDto memberDto);
    List<UserCouponResponseDto> getUserCouponForItem(String category, MemberDto memberDto);
    UserCouponDto.CouponApplyResponse applyCoupon(UserCouponDto.CouponApplyRequest request);
}
