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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.HashSet;
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
    public String updateAddressAndGender(MemberRequestDto req, @LoginMember MemberDto memberDto) {

        log.info("------------------------- OAuth 최초 로그인 시 추가입력");

        log.info("address : " + req.getAddress());
        log.info("gender : " + req.getGender());

        if (req.getGender() == null || req.getAddress().equals("")) {
            return "모든 항목을 입력 해 주세요.";
        }

        Member member = memberRepository.findById(memberDto.getId()).orElseThrow();
        Gender gender = StringToEnumUtil.getEnumFromValue(Gender.class, req.getGender());
        member.updateAddressAndGender(req.getAddress(), gender);
        memberRepository.save(member);
            return "추가정보 입력 완료.";
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

    @Override
    public MemberDto.MemberResponseDto isCurrentPasswordRight(String currentPassword, MemberDto memberDto) {

        log.info("isCurrentPasswordRight : " + bCryptPasswordEncoder.matches(currentPassword, memberDto.getPassword()));
        if (bCryptPasswordEncoder.matches(currentPassword, memberDto.getPassword())) {
            return new MemberDto.MemberResponseDto().builder()
                    .isSuccess(true)
                    .build();
        } else {
            return new MemberDto.MemberResponseDto().builder()
                    .isSuccess(false)
                    .build();
        }
    }

    @Transactional
    @Override
    public ResponseEntity modifyMemberInformation(MemberRequestDto requestDto, MemberDto memberDto) {

        Member member = memberRepository.getReferenceById(memberDto.getId());
        boolean isOauthLogin = Boolean.parseBoolean(requestDto.getIsOauthLogin());

        if (!isOauthLogin) {
            member.updatePassword(bCryptPasswordEncoder.encode(requestDto.getPassword()));
            member.updateEmail(requestDto.getEmail());
        }
        member.updateAddress(requestDto.getAddress());

        Map<String, String> httpBody = new HashMap<>();
        try {
            Member modifiedMember = memberRepository.save(member);
            session.setAttribute("loginMember", MemberDto.fromEntity(modifiedMember));
            httpBody.put("message", "회원 정보 수정 성공");
            return new ResponseEntity(httpBody, HttpStatus.OK);
        } catch (Exception e) {
            log.info("Exception : ", e);
            httpBody.put("message", "회원 정보 수정 실패");
            return new ResponseEntity(httpBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

