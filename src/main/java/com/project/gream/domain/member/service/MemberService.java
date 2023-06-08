package com.project.gream.domain.member.service;

import com.project.gream.common.annotation.LoginMember;
import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.member.dto.MemberRequestDto;
import com.project.gream.domain.order.dto.KakaoPayDto;

import java.util.Map;


public interface MemberService {

    int userRegister(MemberDto memberDto);
    void updateCartItems(KakaoPayDto kakaoPayDto);
    void updateAddressAndGender(MemberRequestDto req, @LoginMember MemberDto memberDto);

}
