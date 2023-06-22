package com.project.gream.domain.member.service;

import com.project.gream.common.annotation.LoginMember;
import com.project.gream.common.enumlist.Gender;
import com.project.gream.common.enumlist.Role;
import com.project.gream.common.util.StringToEnumUtil;
import com.project.gream.domain.member.dto.CartDto;
import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.member.dto.MemberRequestDto;
import com.project.gream.domain.member.entity.Member;
import com.project.gream.domain.member.repository.CartItemRepository;
import com.project.gream.domain.member.repository.MemberRepository;
import com.project.gream.domain.order.dto.KakaoPayDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final HttpSession session;
    private final CartItemRepository cartItemRepository;
    @Override
    @Transactional
    public int userRegister(MemberDto memberDto) {

        log.info("------------------------- Form 회원가입");


        if (memberRepository.existsById(memberDto.getId())) {
            return 1;
            // 아이디 중복
        } else if (memberRepository.existsByEmail(memberDto.getEmail())) {
            return 2;
            // 이메일 중복
        } else if (!memberRepository.existsById(memberDto.getId())) {
            memberDto.setPassword(bCryptPasswordEncoder.encode(memberDto.getPassword()));
            memberDto.setRole(Role.MEMBER);
            memberDto.setCartDto(new CartDto());
            Member member = memberDto.toEntity();
            memberRepository.save(member);
        }
        return 3; // 회원가입 성공
    }

    @Transactional
    @Override
    public void updateAddressAndGender(MemberRequestDto req, @LoginMember MemberDto memberDto) {

        log.info("------------------------- OAuth 최초 로그인 시 추가입력");

        Member member = memberDto.toEntity();
        Gender gender = StringToEnumUtil.getEnumFromValue(Gender.class, req.getGender());
        member.updateAddressAndGender(req.getAddress(), gender);
        memberRepository.save(member);
    }

    @Override
    public void updateCartItems(KakaoPayDto kakaoPayDto) {

        log.info("------------------------- 결제 완료된 장바구니 상품 삭제");

//        String[] cartItemArray = kakaoPayDto.getCartItemIds().split("/");

//        for (String cartItemId : cartItemArray) {
//            cartItemRepository.deleteById(Long.parseLong(cartItemId));
//        }

        List<Long> cartItemArray = kakaoPayDto.getCartItemIds();
        for (Long cartItemId : cartItemArray) {
            cartItemRepository.deleteById(cartItemId);
        }
    }
}

